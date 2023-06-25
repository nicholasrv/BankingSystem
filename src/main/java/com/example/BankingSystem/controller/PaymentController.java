package com.example.BankingSystem.controller;

import com.example.BankingSystem.dtos.PaymentDTO;
import com.example.BankingSystem.dtos.PaymentResponseDTO;
import com.example.BankingSystem.dtos.TransactionBillingDTO;
import com.example.BankingSystem.exceptions.BadRequestException;
import com.example.BankingSystem.model.BankAccount;
import com.example.BankingSystem.model.Payment;
import com.example.BankingSystem.model.Transaction;
import com.example.BankingSystem.model.TransactionType;
import com.example.BankingSystem.service.impl.BankAccountService;
import com.example.BankingSystem.service.impl.PaymentService;
import com.example.BankingSystem.service.impl.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping("/billing/save")
    @Transactional
    public PaymentResponseDTO payABill(@RequestBody PaymentDTO paymentDTO) throws SQLException, BadRequestException{

        // Get the bank account
        BankAccount bankAccount = bankAccountService.searchById(paymentDTO.getIdAccount()).orElse(null);

        // Create the Transaction object and sets all it's params/values
        Transaction transaction = new Transaction();
        transaction.setAmount(paymentDTO.getAmount());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setType(TransactionType.BILLING);
        transaction.setSourceAccount(bankAccount);

        // Process and persist the transaction onto the database
        transaction = transactionService.save(transaction);

        // Subtract the bank account's balance for the transaction amount, and then update the account's balance afterwards
        bankAccount.setBalance(bankAccount.getBalance() - transaction.getAmount());
        bankAccountService.update(bankAccount);

        //Check if the bill was already paid/registered on the database, and then finally saves the payment.
        Payment payment = new Payment(paymentDTO.getBillNumber(), bankAccount, transaction);
        boolean isThisBillAlreadyPaid = paymentService.existsPaymentByBillNumber(paymentDTO.getBillNumber());
        if(isThisBillAlreadyPaid) {
            throw new BadRequestException("The bill you're trying to pay was already paid and registered in our system");
        }
        paymentService.save(payment);

        //Links the payment with the transaction;
//        transaction.setPayment(paymentService.searchById(paymentDTO.getIdTransaction()).orElse(null));
        transaction.setPayment(paymentService.searchById(payment.getId()).orElse(null));
        transactionService.update(transaction);

        // Returns the response body
        PaymentResponseDTO responseDTO = new PaymentResponseDTO();
        responseDTO.setMessage("Payment successful!");

        return responseDTO;
    }


}

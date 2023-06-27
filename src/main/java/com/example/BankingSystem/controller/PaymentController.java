package com.example.BankingSystem.controller;

import com.example.BankingSystem.dtos.*;
import com.example.BankingSystem.exceptions.BadRequestException;
import com.example.BankingSystem.exceptions.ResourceNotFoundException;
import com.example.BankingSystem.model.BankAccount;
import com.example.BankingSystem.model.Payment;
import com.example.BankingSystem.model.Transaction;
import com.example.BankingSystem.model.TransactionType;
import com.example.BankingSystem.service.impl.BankAccountService;
import com.example.BankingSystem.service.impl.PaymentService;
import com.example.BankingSystem.service.impl.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
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
        transaction.setPayment(paymentService.searchById(payment.getId()).orElse(null));
        transactionService.update(transaction);

        // Returns the response body
        return payment.responseBillingDTO();
    }

    @PostMapping("/transfer/save")
    @Transactional
    public PaymentTransferResponseDTO makeATransfer(@RequestBody PaymentTransferDTO paymentTransferDTO) throws SQLException, BadRequestException{
        //Get both source and destination accounts
        BankAccount sourceBankAccount = bankAccountService.searchById(paymentTransferDTO.getIdSourceAccount()).orElse(null);
        BankAccount destinationBankAccount = bankAccountService.searchById(paymentTransferDTO.getIdDestinationAccount()).orElse(null);

        //Create the Transaction object and sets all it's params/values
        Transaction transaction = new Transaction();
        transaction.setAmount(paymentTransferDTO.getAmount());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setType(TransactionType.TRANSFER);
        transaction.setSourceAccount(sourceBankAccount);
        transaction.setDestinationAccount(destinationBankAccount);

        transaction = transactionService.save(transaction);

        // Update the balance on both accounts
        sourceBankAccount.setBalance(sourceBankAccount.getBalance() - transaction.getAmount());
        destinationBankAccount.setBalance(destinationBankAccount.getBalance() + transaction.getAmount());
        bankAccountService.update(sourceBankAccount);
        bankAccountService.update(destinationBankAccount);

        // Create and save the payment onto the database
        Payment payment = new Payment("n/a", sourceBankAccount, transaction);
        paymentService.save(payment);

        // Links the payment with the transaction
        transaction.setPayment(paymentService.searchById(payment.getId()).orElse(null));
        transactionService.update(transaction);

        // return the response
        return payment.responseTransferDTO();
    }

    @PutMapping("/update")
    public ResponseEntity updatePayment(@RequestBody Payment payment) throws SQLException {
        return ResponseEntity.ok(paymentService.update(payment));
    }

    @RequestMapping(value = "/values", method = RequestMethod.GET, produces = "application/json")
    public List<Payment> getAllPayments() throws SQLException {
        return paymentService.getAllResults();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Payment>> getPaymentById(@PathVariable Long id) throws ResourceNotFoundException {
        try {
            Optional<Payment> payment = paymentService.searchById(id);
            if (payment.isPresent()) {
                return ResponseEntity.ok(payment);
            }
            throw new ResourceNotFoundException("The payment with id number " + id + "hasn't been found in the database.");
        } catch (SQLException e) {
            throw new ResourceNotFoundException("Error while searching payment with id number" + id + ". Please contact our support team for further information/instructions.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable("id") Long paymentId) throws ChangeSetPersister.NotFoundException {
        if (paymentService.existsTransactionByPayment(paymentId)) {
            paymentService.delete(paymentId);
            ResponseEntity.ok("The selected payment was successfully deleted.");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("The selected payment cannot be deleted, as there are transactions associated to it.");
    }

}

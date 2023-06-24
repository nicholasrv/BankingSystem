package com.example.BankingSystem.controller;

import com.example.BankingSystem.dtos.PaymentDTO;
import com.example.BankingSystem.dtos.PaymentResponseDTO;
import com.example.BankingSystem.dtos.TransactionBillingDTO;
import com.example.BankingSystem.model.BankAccount;
import com.example.BankingSystem.model.Transaction;
import com.example.BankingSystem.service.impl.BankAccountService;
import com.example.BankingSystem.service.impl.PaymentService;
import com.example.BankingSystem.service.impl.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping("save")
    @Transactional
    public PaymentResponseDTO makePayment(@RequestBody PaymentDTO paymentDTO) throws SQLException{
        BankAccount bankAccount = bankAccountService.searchById(paymentDTO.getIdAccount()).orElse(null);
        Transaction TransactionBillingDTO = new Transaction();
        Transaction transaction = transactionService.save(TransactionBillingDTO)
    }



}

package com.example.BankingSystem.repository;

import com.example.BankingSystem.model.Payment;
import com.example.BankingSystem.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    boolean existsByPayment(Payment payment);
}

package com.example.BankingSystem.repository;

import com.example.BankingSystem.model.Transaction;
import com.example.BankingSystem.service.BankingSystem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

package com.example.BankingSystem.service.impl;

import com.example.BankingSystem.model.Transaction;
import com.example.BankingSystem.model.UserEntity;
import com.example.BankingSystem.repository.TransactionRepository;
import com.example.BankingSystem.repository.UserRepository;
import com.example.BankingSystem.service.BankingSystem;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements BankingSystem<Transaction> {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        if(transaction != null){
            return transactionRepository.save(transaction);
        }
        return new Transaction();
    }

    @Override
    public String update(Transaction transaction) {
        if (transaction != null && transactionRepository.findById(transaction.getId()).isPresent()){
            transactionRepository.saveAndFlush(transaction);
            return "The requested transaction was successfully updated!";
        }
        return "Sorry, but the requested transaction couldn't be found";
    }

    @Override
    public List<Transaction> getAllResults() throws SQLException {
        return transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> searchById(Long id) throws SQLException {
        return transactionRepository.findById(id);
    }

    @Override
    public boolean delete(Long id) {
        if(transactionRepository.findById(id).isPresent()){
            transactionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

package com.example.BankingSystem.service.impl;

import com.example.BankingSystem.model.BankAccount;
import com.example.BankingSystem.model.Transaction;
import com.example.BankingSystem.repository.BankAccountRepository;
import com.example.BankingSystem.service.BankingSystem;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BankAccountService implements BankingSystem<BankAccount> {

    private final BankAccountRepository bankAccountRepository;
    public BankAccountService(BankAccountRepository bankAccountRepository){
        this.bankAccountRepository = bankAccountRepository;
    }
    @Override
    public BankAccount save(BankAccount bankAccount) {
        if(bankAccount != null){
            return bankAccountRepository.save(bankAccount);
        }
        return new BankAccount();
    }

    @Override
    public String update(BankAccount bankAccount) {
        if (bankAccount != null && bankAccountRepository.findById(bankAccount.getId()).isPresent()){
            bankAccountRepository.saveAndFlush(bankAccount);
            return "The requested bank account was successfully updated!";
        }
        return "Sorry, but the requested bank account couldn't be found";
    }

    @Override
    public List<BankAccount> getAllResults() throws SQLException {
        return bankAccountRepository.findAll();
    }

    @Override
    public Optional<BankAccount> searchById(Long id) throws SQLException {
        return bankAccountRepository.findById(id);
    }

    @Override
    public boolean delete(Long id) {
        if(bankAccountRepository.findById(id).isPresent()){
            bankAccountRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

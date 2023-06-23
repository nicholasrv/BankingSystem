package com.example.BankingSystem.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BankingSystem <T>{
    public T save (T t);

    public String update (T t);

    public List<T> getAllResults() throws SQLException;

    public Optional<T> searchById(Long id) throws SQLException;

    public boolean delete(Long id) throws SQLException;
}

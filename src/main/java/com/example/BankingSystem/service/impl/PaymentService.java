package com.example.BankingSystem.service.impl;

import com.example.BankingSystem.model.Payment;
import com.example.BankingSystem.repository.PaymentRepository;
import com.example.BankingSystem.service.BankingSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService implements BankingSystem<Payment> {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment save(Payment payment) {
        if(payment != null){
            return paymentRepository.save(payment);
        }
        return new Payment();
    }

    @Override
    public String update(Payment payment) {
        if (payment != null && paymentRepository.findById(payment.getId()).isPresent()){
            paymentRepository.saveAndFlush(payment);
            return "The requested payment was successfully updated!";
        }
        return "Sorry, but the requested payment couldn't be found";
    }

    @Override
    public List<Payment> getAllResults() throws SQLException {
        return paymentRepository.findAll();
    }

    @Override
    public Optional<Payment> searchById(Long id) throws SQLException {
        return paymentRepository.findById(id);
    }

    @Override
    public boolean delete(Long id) {
        if(paymentRepository.findById(id).isPresent()){
            paymentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existsPaymentByBillNumber(String billNumber) {
        return paymentRepository.existsPaymentByBillNumber(billNumber);
    }
}

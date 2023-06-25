package com.example.BankingSystem.dtos;

import com.example.BankingSystem.model.BankAccount;
import com.example.BankingSystem.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private String billNumber;
    private BankAccount bankAccount;
    private Transaction transaction;

    public PaymentResponseDTO(String billNumber, BankAccountDTO bankAccountDTO, TransactionBillingDTO transactionBillingDTO) {
    }

    public void setMessage(String s) {
    }
}

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
public class PaymentResponseDTO {

    private String billNumber;
    private Transaction transaction;
    private BankAccount account;

        public PaymentResponseDTO(String billNumber, TransactionBillingDTO transactionBillingDTO, BankAccountDTO bankAccountDTO) {
        }

}

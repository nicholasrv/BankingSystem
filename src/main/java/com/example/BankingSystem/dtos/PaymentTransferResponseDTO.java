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
public class PaymentTransferResponseDTO {
    private Transaction transaction;
    private BankAccount sourceAccount;
    private BankAccount destinationAccount;

    public PaymentTransferResponseDTO(TransactionTransferDTO transactionTransferDTO, BankAccountDTO sourceAccount, BankAccountDTO destinationAccount) {
    }

}

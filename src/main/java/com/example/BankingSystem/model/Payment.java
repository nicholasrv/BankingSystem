package com.example.BankingSystem.model;

import com.example.BankingSystem.dtos.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String billNumber;

    @ManyToOne
    @JoinColumn(name = "source_account_id", nullable = false)
    private BankAccount sourceAccount;

    @OneToOne(mappedBy = "payment")
    @JoinColumn(name = "transaction_id")
    @JsonIgnore
    private Transaction transaction;

    public Payment(String billNumber, BankAccount sourceAccount, Transaction transaction) {
        this.billNumber = billNumber;
        this.sourceAccount = sourceAccount;
        this.transaction = transaction;
    }

    public PaymentResponseDTO responseBillingDTO(){
        TransactionBillingDTO transactionBillingDTO = new TransactionBillingDTO(this.transaction.getAmount(), this.transaction.getTransactionDate(), this.transaction.getType());
        BankAccountDTO bankAccountDTO = new BankAccountDTO(this.sourceAccount.getId(), this.sourceAccount.getAccountNumber(), this.sourceAccount.getBalance(), this.sourceAccount.getAccountType(), this.sourceAccount.getId());

        return new PaymentResponseDTO(
                this.billNumber,
                transactionBillingDTO,
                bankAccountDTO
        );
    }

    public PaymentTransferResponseDTO responseTransferDTO(){
        TransactionTransferDTO transactionTransferDTO = new TransactionTransferDTO(this.transaction.getAmount(), this.transaction.getTransactionDate(), this.transaction.getType());
        BankAccountDTO sourceAccount = new BankAccountDTO(this.sourceAccount.getId(), this.sourceAccount.getAccountNumber(), this.sourceAccount.getBalance(), this.sourceAccount.getAccountType(), this.sourceAccount.getId());
        BankAccountDTO destinationAccount = new BankAccountDTO(this.sourceAccount.getId(), this.sourceAccount.getAccountNumber(), this.sourceAccount.getBalance(), this.sourceAccount.getAccountType(), this.sourceAccount.getId());

        return new PaymentTransferResponseDTO(
                transactionTransferDTO,
                sourceAccount,
                destinationAccount
        );
    }

}

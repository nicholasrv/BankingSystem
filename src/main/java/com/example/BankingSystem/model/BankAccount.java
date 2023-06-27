package com.example.BankingSystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "bank_accounts")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private double balance;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private String accountType;

    public BankAccount(String accountNumber, double balance, UserEntity user, String accountType) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.user = user;
        this.accountType = accountType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BankAccount other = (BankAccount) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(accountNumber, other.accountNumber)
                && Objects.equals(balance, other.balance)
                && Objects.equals(user, other.user)
                && accountType == other.accountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, balance, user, accountType);
    }

}

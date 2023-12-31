package com.example.BankingSystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDTO {
    private Long idAccount;
    private String accountNumber;
    private Double balance;
    private String accountType;
    private Long idUser;
}

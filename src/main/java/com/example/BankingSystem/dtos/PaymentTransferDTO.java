package com.example.BankingSystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransferDTO {
    private Double amount;

    private Long idSourceAccount;

    private Long idDestinationAccount;
}

package com.example.BankingSystem.test;

import com.example.BankingSystem.controller.PaymentController;
import com.example.BankingSystem.dtos.PaymentDTO;
import com.example.BankingSystem.dtos.PaymentResponseDTO;
import com.example.BankingSystem.exceptions.BadRequestException;
import com.example.BankingSystem.model.BankAccount;
import com.example.BankingSystem.model.Payment;
import com.example.BankingSystem.model.Transaction;
import com.example.BankingSystem.service.impl.BankAccountService;
import com.example.BankingSystem.service.impl.PaymentService;
import com.example.BankingSystem.service.impl.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PaymentControllerTests {

    @Mock
    private BankAccountService bankAccountService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    public void testPayABill() throws SQLException, BadRequestException {
        // Mocking the dependencies
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        bankAccount.setBalance(1000.0);

        Transaction transaction = new Transaction();
        transaction.setId(1L);

        Payment payment = new Payment();
        payment.setId(1L);

        when(bankAccountService.searchById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(bankAccount));
        when(transactionService.save(ArgumentMatchers.any(Transaction.class))).thenReturn(transaction);
        when(paymentService.existsPaymentByBillNumber(ArgumentMatchers.anyString())).thenReturn(false);
        when(paymentService.save(ArgumentMatchers.any(Payment.class))).thenReturn(payment);
        when(paymentService.searchById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(payment));

        // Creating the PaymentDTO object for testing
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setIdAccount(1L);
        paymentDTO.setAmount(500.0);
        paymentDTO.setBillNumber("123456");

        // Calling the payABill method
        ResponseEntity<PaymentResponseDTO> responseEntity = paymentController.payABill(paymentDTO);

        // Asserting the response
        assertEquals(200, responseEntity.getStatusCodeValue());

        PaymentResponseDTO responseDTO = responseEntity.getBody();
        assertEquals("Payment successful!", responseDTO);
    }
}


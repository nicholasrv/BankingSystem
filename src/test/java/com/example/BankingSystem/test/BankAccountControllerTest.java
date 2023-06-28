package com.example.BankingSystem.test;

import com.example.BankingSystem.controller.BankAccountController;
import com.example.BankingSystem.dtos.BankAccountDTO;
import com.example.BankingSystem.exceptions.BadRequestException;
import com.example.BankingSystem.model.BankAccount;
import com.example.BankingSystem.model.UserEntity;
import com.example.BankingSystem.service.impl.BankAccountService;
import com.example.BankingSystem.service.impl.UserService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class BankAccountControllerTest {

    @Mock
    private BankAccountService bankAccountService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BankAccountController bankAccountController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @org.junit.Test
    public void testSetupAnAccount() throws BadRequestException, SQLException {
        // Create a BankAccountDTO object with the necessary data
        BankAccountDTO bankAccountDTO = new BankAccountDTO();
        bankAccountDTO.setAccountNumber("123456789");
        bankAccountDTO.setBalance(1000.0);
        bankAccountDTO.setIdUser(1L);
        bankAccountDTO.setAccountType("savings");

        // Mock the behavior of userService.searchById() method
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(userService.searchById(bankAccountDTO.getIdUser())).thenReturn(Optional.of(userEntity));

        // Mock the behavior of bankAccountService.existsBankAccountByAccountNumber() method
        when(bankAccountService.existsBankAccountByAccountNumber(bankAccountDTO.getAccountNumber())).thenReturn(false);

        // Mock the behavior of bankAccountService.save() method
        BankAccount savedBankAccount = new BankAccount();
        savedBankAccount.setId(1L);
        savedBankAccount.setAccountNumber(bankAccountDTO.getAccountNumber());
        savedBankAccount.setBalance(bankAccountDTO.getBalance());
        savedBankAccount.setUser(userEntity);
        savedBankAccount.setAccountType(bankAccountDTO.getAccountType());
        when(bankAccountService.save(ArgumentMatchers.any(BankAccount.class))).thenReturn(savedBankAccount);

        // Call the method under test
        ResponseEntity<?> responseEntity = bankAccountController.setupAnAccount(bankAccountDTO);

        // Perform assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Account successfully opened!" + savedBankAccount.toString(), responseEntity.getBody());

        // Verify the interactions with mocked dependencies
        Mockito.verify(userService, atLeastOnce()).searchById(bankAccountDTO.getIdUser());
        Mockito.verify(bankAccountService, atLeastOnce()).existsBankAccountByAccountNumber(bankAccountDTO.getAccountNumber());
        Mockito.verify(bankAccountService, atLeastOnce()).save(ArgumentMatchers.any(BankAccount.class));
    }

    @org.junit.Test
    public void testUpdateAccountDetails() throws SQLException {
        // Create a BankAccountDTO object with the necessary data
        BankAccountDTO bankAccountDTO = new BankAccountDTO();
        bankAccountDTO.setIdAccount(3L);
        bankAccountDTO.setAccountNumber("123456789");
        bankAccountDTO.setBalance(1500.0);
        bankAccountDTO.setIdUser(1L);
        bankAccountDTO.setAccountType("savings");

        //Mock the behavior of bankAccountService.searchById() method
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(3L);
        when(bankAccountService.searchById(bankAccountDTO.getIdAccount())).thenReturn(Optional.of(bankAccount));


        // Mock the behavior of userService.searchById() method
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        when(userService.searchById(bankAccountDTO.getIdUser())).thenReturn(Optional.of(userEntity));

        // Mock the behavior of bankAccountService.update() method
        BankAccount updatedBankAccount = new BankAccount();
        updatedBankAccount.setId(3L);
        updatedBankAccount.setAccountNumber(bankAccountDTO.getAccountNumber());
        updatedBankAccount.setBalance(bankAccountDTO.getBalance());
        updatedBankAccount.setUser(userEntity);
        updatedBankAccount.setAccountType(bankAccountDTO.getAccountType());
        when(bankAccountService.update(ArgumentMatchers.any(BankAccount.class))).thenReturn(String.valueOf(updatedBankAccount));

        // Call the method under test
        ResponseEntity responseEntity = bankAccountController.updateAccountDetails(bankAccountDTO);

        // Perform assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedBankAccount, responseEntity.getBody());

        // Verify the interactions with mocked dependencies
        Mockito.verify(userService, atLeastOnce()).searchById(anyLong());
        Mockito.verify(bankAccountService, atLeastOnce()).update(ArgumentMatchers.any(BankAccount.class));
    }




}
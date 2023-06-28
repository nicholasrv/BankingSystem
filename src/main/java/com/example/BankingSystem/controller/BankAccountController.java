package com.example.BankingSystem.controller;

import com.example.BankingSystem.dtos.BankAccountDTO;
import com.example.BankingSystem.exceptions.BadRequestException;
import com.example.BankingSystem.exceptions.ResourceNotFoundException;
import com.example.BankingSystem.model.BankAccount;
import com.example.BankingSystem.model.UserEntity;
import com.example.BankingSystem.service.impl.BankAccountService;
import com.example.BankingSystem.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bankaccounts")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserService userService;

    /// CREATE A NEW ACCOUNT
    @PostMapping("/save")
    public ResponseEntity<?> setupAnAccount(@RequestBody BankAccountDTO bankAccountDTO) throws BadRequestException {
        try {
            UserEntity userEntity = userService.searchById(bankAccountDTO.getIdUser()).orElse(null);
            BankAccount bankAccount = new BankAccount(bankAccountDTO.getAccountNumber(), bankAccountDTO.getBalance(), userEntity, bankAccountDTO.getAccountType());
            boolean doesThisAccountExist = bankAccountService.existsBankAccountByAccountNumber(bankAccount.getAccountNumber());
            if (doesThisAccountExist){
                return ResponseEntity.badRequest().body("This account already exists on the database.");
            }
            return ResponseEntity.ok("Account successfully opened!" + bankAccountService.save(bankAccount));
        } catch (Exception e){
            e.printStackTrace();
            throw new BadRequestException("An error has occurred while trying to open up this account. Please contact our support team for further information.");
        }
    }

    ///UPDATE/PUT
    @PutMapping("/update")
    public ResponseEntity updateAccountDetails(@RequestBody BankAccountDTO bankAccountDTO) throws SQLException {
        BankAccount idAcc = bankAccountService.searchById(bankAccountDTO.getIdAccount()).orElse(null);
        UserEntity userEntity = userService.searchById(bankAccountDTO.getIdUser()).orElse(null);
        assert idAcc != null;
        BankAccount bankAccount = new BankAccount(idAcc.getId(), bankAccountDTO.getAccountNumber(), bankAccountDTO.getBalance(), userEntity, bankAccountDTO.getAccountType());
        return ResponseEntity.ok(bankAccountService.update(bankAccount));
    }

    // GET
    @RequestMapping(value = "/values", method = RequestMethod.GET, produces = "application/json")
    public List<BankAccount> getAllAccounts() throws SQLException{
        return bankAccountService.getAllResults();
    }


    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteAccount(@PathVariable Long id) throws ResourceNotFoundException, SQLException{
        boolean haveItDeleted = bankAccountService.delete(id);
        if(haveItDeleted){
            return ResponseEntity.ok("The selected account has been successfully deleted from the database!");
        }
        else{
            throw new ResourceNotFoundException("The account with id number " + id + "hasn't been found in the database.");
        }
    }

    //GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<BankAccount>> getBankAccountById(@PathVariable Long id) throws ResourceNotFoundException{
        try{
            Optional<BankAccount> bankAccount = bankAccountService.searchById(id);
            if(bankAccount.isPresent()){
                return ResponseEntity.ok(bankAccount);
            }
            throw new ResourceNotFoundException("The bank account with id number " + id + "hasn't been found in the database.");
        } catch (SQLException e) {
            throw new ResourceNotFoundException("Error while searching bank account with id number" + id + ". Please contact our support team for further information/instructions.");
        }
    }

}

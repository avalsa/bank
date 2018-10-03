package com.avalsa.bankaccount.controller;

import com.avalsa.bankaccount.controller.validation.exception.ValidationException;
import com.avalsa.bankaccount.model.BankAccount;
import com.avalsa.bankaccount.service.BankAccountService;
import com.avalsa.bankaccount.service.exception.AccountNotFoundException;
import com.avalsa.bankaccount.service.exception.ExistingAccountException;
import com.avalsa.bankaccount.service.exception.NegativeBalanceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.avalsa.bankaccount.controller.validation.Checks.validateAccountId;
import static com.avalsa.bankaccount.controller.validation.Checks.validateAmount;


@RestController
public class BankAccountController {
    private final BankAccountService service;

    public BankAccountController(BankAccountService service) {
        this.service = service;
    }

    @PostMapping("/bankaccount/{id}")
    public ResponseEntity<String> createBankAccount(@PathVariable Integer id) throws ValidationException, ExistingAccountException {
        validateAccountId(id);
        service.createBankAccount(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/bankaccount/{id}/deposit")
    public ResponseEntity<String> depositBankAccount(@PathVariable Integer id, @RequestBody Integer amount) throws AccountNotFoundException, ValidationException {
        validateAccountId(id);
        validateAmount(amount);
        service.deposit(id, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/bankaccount/{id}/withdraw")
    public ResponseEntity<String> withdrawBankAccount(@PathVariable Integer id, @RequestBody Integer amount) throws AccountNotFoundException, NegativeBalanceException, ValidationException {
        validateAccountId(id);
        validateAmount(amount);
        service.withdraw(id, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/bankaccount/{id}/balance")
    public ResponseEntity<Integer> withdrawBankAccount(@PathVariable Integer id) throws ValidationException, AccountNotFoundException {
        validateAccountId(id);
        BankAccount bankAccount = service.getBankAccount(id);
        return new ResponseEntity<>(bankAccount.getBalance(), HttpStatus.OK);
    }

}

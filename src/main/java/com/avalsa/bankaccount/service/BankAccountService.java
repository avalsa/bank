package com.avalsa.bankaccount.service;

import com.avalsa.bankaccount.service.exception.AccountNotFoundException;
import com.avalsa.bankaccount.model.BankAccount;
import com.avalsa.bankaccount.service.exception.ExistingAccountException;
import com.avalsa.bankaccount.service.exception.NegativeBalanceException;

public interface BankAccountService {
    void createBankAccount(Integer id) throws ExistingAccountException;

    BankAccount getBankAccount(Integer id) throws AccountNotFoundException;

    void deposit(Integer id, Integer amount) throws AccountNotFoundException;

    void withdraw(Integer id, Integer amount) throws AccountNotFoundException, NegativeBalanceException;
}

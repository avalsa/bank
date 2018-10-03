package com.avalsa.bankaccount.service;

import com.avalsa.bankaccount.service.exception.AccountNotFoundException;
import com.avalsa.bankaccount.model.BankAccount;
import com.avalsa.bankaccount.service.exception.ExistingAccountException;
import com.avalsa.bankaccount.repository.BankAccountRepository;
import com.avalsa.bankaccount.service.exception.NegativeBalanceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    @Transactional
    public void createBankAccount(Integer id) throws ExistingAccountException {
        if (bankAccountRepository.existsById(id))
            throw new ExistingAccountException();
        BankAccount ba = new BankAccount();
        ba.setId(id);
        ba.setBalance(0);
        bankAccountRepository.save(ba);
    }

    @Override
    public BankAccount getBankAccount(Integer id) throws AccountNotFoundException {
        return bankAccountRepository.findById(id).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    @Transactional
    public void deposit(Integer id, Integer amount) throws AccountNotFoundException {
        BankAccount bankAccount = getBankAccount(id);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    @Transactional
    public void withdraw(Integer id, Integer amount) throws AccountNotFoundException, NegativeBalanceException {
        BankAccount bankAccount = getBankAccount(id);
        int newBalance = bankAccount.getBalance() - amount;
        if (newBalance < 0)
            throw new NegativeBalanceException();
        bankAccount.setBalance(newBalance);
        bankAccountRepository.save(bankAccount);
    }
}

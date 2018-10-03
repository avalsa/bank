package com.avalsa.bankaccount.repository;

import com.avalsa.bankaccount.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
}

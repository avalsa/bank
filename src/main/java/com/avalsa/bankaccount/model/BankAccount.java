package com.avalsa.bankaccount.model;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
public class BankAccount {

    @Id
    @Range(min = 10000, max = 99999, message = "5 digits")
    private Integer id;

    @PositiveOrZero
    @NotNull
    @ColumnDefault("0")
    private Integer balance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}

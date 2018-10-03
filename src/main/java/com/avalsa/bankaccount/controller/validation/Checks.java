package com.avalsa.bankaccount.controller.validation;

import com.avalsa.bankaccount.controller.validation.exception.ValidationException;

public final class Checks {

    private Checks() {}

    public static void validateAccountId(Integer id) throws ValidationException {
        if (id == null || id < 10000 || id > 99999) throw new ValidationException("incorrect id (positive number with 5 digits required)");
    }

    public static void validateAmount(Integer amount) throws ValidationException {
        if (amount == null || amount < 0) throw new ValidationException("incorrect amount (positive integer required)");
    }
}

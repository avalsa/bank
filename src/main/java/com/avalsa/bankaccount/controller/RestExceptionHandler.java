package com.avalsa.bankaccount.controller;

import com.avalsa.bankaccount.controller.validation.exception.ValidationException;
import com.avalsa.bankaccount.service.exception.AccountNotFoundException;
import com.avalsa.bankaccount.service.exception.ExistingAccountException;
import com.avalsa.bankaccount.service.exception.NegativeBalanceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static jdk.nashorn.internal.parser.JSONParser.quote;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    protected ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException e) {
        return new ResponseEntity<>(quote("account don't found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<String> handleValidationException(ValidationException e) {
        return new ResponseEntity<>(quote(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NegativeBalanceException.class)
    protected ResponseEntity<String> handleNegativeBalanceException(NegativeBalanceException e) {
        return new ResponseEntity<>(quote("insufficient funds"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExistingAccountException.class)
    protected ResponseEntity<String> handleExistingAccountException(ExistingAccountException e) {
        return new ResponseEntity<>(quote("account with such id already exists"), HttpStatus.BAD_REQUEST);
    }
}

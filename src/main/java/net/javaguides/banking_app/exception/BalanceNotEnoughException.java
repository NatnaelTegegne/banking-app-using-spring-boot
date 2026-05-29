package net.javaguides.banking_app.exception;

import org.springframework.web.bind.annotation.ResponseStatus;


public class BalanceNotEnoughException extends RuntimeException {

    public BalanceNotEnoughException(String message) {
        super(message);
    }
}

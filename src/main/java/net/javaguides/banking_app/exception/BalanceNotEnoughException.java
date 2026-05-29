package net.javaguides.banking_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class BalanceNotEnoughException extends RuntimeException {

    public BalanceNotEnoughException(String message) {
        super(message);
    }
}

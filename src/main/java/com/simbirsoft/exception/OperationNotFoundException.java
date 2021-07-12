package com.simbirsoft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.OK, reason = "Operation not found")
public class OperationNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Operations not found";

    public OperationNotFoundException() {
        super(MESSAGE);
    }
}

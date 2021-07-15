package com.simbirsoft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.OK, reason = "Incorrect date")
public class IncorrectDateException extends RuntimeException {
    private static final String MESSAGE = "Incorrect date";

    public IncorrectDateException() {
        super(MESSAGE);
    }
}

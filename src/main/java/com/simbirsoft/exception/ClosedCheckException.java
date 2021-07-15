package com.simbirsoft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.OK, reason = "Check is closed")
public class ClosedCheckException extends RuntimeException {
    private static final String MESSAGE = "Check is closed";

    public ClosedCheckException() {
        super(MESSAGE);
    }
}

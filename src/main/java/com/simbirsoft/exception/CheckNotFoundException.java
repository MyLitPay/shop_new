package com.simbirsoft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.OK, reason = "Check not found")
public class CheckNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Checks not found";
    public CheckNotFoundException() {
        super(MESSAGE);
    }
}

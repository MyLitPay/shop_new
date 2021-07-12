package com.simbirsoft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.OK, reason = "Check not found")
public class CheckNotFoundException extends RuntimeException {
    public CheckNotFoundException(String message) {
        super(message);
    }
}

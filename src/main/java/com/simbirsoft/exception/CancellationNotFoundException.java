package com.simbirsoft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Invoice not found")
public class CancellationNotFoundException extends RuntimeException {
    public CancellationNotFoundException(String message) {
        super(message);
    }
}

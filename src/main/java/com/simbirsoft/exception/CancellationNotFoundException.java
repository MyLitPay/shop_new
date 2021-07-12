package com.simbirsoft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.OK, reason = "Cancellation not found")
public class CancellationNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Cancellations not found";
    public CancellationNotFoundException() {
        super(MESSAGE);
    }
}

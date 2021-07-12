package com.simbirsoft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Product don't set")
public class ProductNotSetException extends RuntimeException {
    public ProductNotSetException(String message) {
        super(message);
    }
}

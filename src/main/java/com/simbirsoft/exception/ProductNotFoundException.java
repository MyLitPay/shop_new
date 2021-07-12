package com.simbirsoft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.OK, reason = "Product not found")
public class ProductNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Products not found";

    public ProductNotFoundException() {
        super(MESSAGE);
    }
}

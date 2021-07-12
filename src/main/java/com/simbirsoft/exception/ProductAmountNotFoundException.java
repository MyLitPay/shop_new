package com.simbirsoft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.OK, reason = "Amount not found")
public class ProductAmountNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Product amounts not found";

    public ProductAmountNotFoundException() {
        super(MESSAGE);
    }
}

package com.simbirsoft.exception;

import java.util.List;

public class ProductAmountNotEnoughException extends RuntimeException {
    private List<String> errors;

    public ProductAmountNotEnoughException(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}

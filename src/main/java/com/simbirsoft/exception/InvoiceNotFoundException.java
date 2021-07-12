package com.simbirsoft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.OK, reason = "Invoice not found")
public class InvoiceNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Invoices not found";

    public InvoiceNotFoundException() {
        super(MESSAGE);
    }
}

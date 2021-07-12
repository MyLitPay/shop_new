package com.simbirsoft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.OK, reason = "Group not found")
public class GroupNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Groups not found";

    public GroupNotFoundException() {
        super(MESSAGE);
    }
}

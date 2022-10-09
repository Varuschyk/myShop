package com.alexvar.springboot_rest.exception;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String arg) {
        super(arg);
    }
}

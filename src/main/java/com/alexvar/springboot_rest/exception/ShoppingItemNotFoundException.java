package com.alexvar.springboot_rest.exception;

import javax.persistence.EntityNotFoundException;

public class ShoppingItemNotFoundException extends EntityNotFoundException {

    public ShoppingItemNotFoundException() {
        super();
    }

    public ShoppingItemNotFoundException(String arg) {
        super(arg);
    }
}

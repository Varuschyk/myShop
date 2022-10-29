package com.alexvar.springboot_rest.exception;

import javax.persistence.EntityExistsException;

public class ShoppingItemExistsException extends EntityExistsException {
    public ShoppingItemExistsException() {
        super();
    }

    public ShoppingItemExistsException(String message) {
        super(message);
    }
}

package com.alexvar.springboot_rest.exception;

public class ApiShoppingItemException extends Exception{
    public ApiShoppingItemException() {
    }

    public ApiShoppingItemException(String message) {
        super(message);
    }
}

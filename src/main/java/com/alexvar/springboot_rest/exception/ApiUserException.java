package com.alexvar.springboot_rest.exception;

public class ApiUserException extends Exception{
    public ApiUserException() {
    }

    public ApiUserException(String message) {
        super(message);
    }
}

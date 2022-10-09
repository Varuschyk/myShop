package com.alexvar.springboot_rest.exception;

public class NullEntityException extends NullPointerException{
    public NullEntityException() {
        super();
    }

    public NullEntityException(String s) {
        super(s);
    }
}

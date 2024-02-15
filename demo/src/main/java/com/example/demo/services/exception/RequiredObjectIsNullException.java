package com.example.demo.services.exception;

public class RequiredObjectIsNullException extends RuntimeException {

    public RequiredObjectIsNullException() {
        super("It's not allowed to persist a null object!");
    }

    public RequiredObjectIsNullException(String msg) {
        super(msg);
    }
}

package com.codecool.spring.rest.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String msg) {
        super(msg);
    }
}

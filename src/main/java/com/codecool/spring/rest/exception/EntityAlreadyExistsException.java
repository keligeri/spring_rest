package com.codecool.spring.rest.exception;

/**
 * Created by keli on 2017.11.14..
 */
public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}

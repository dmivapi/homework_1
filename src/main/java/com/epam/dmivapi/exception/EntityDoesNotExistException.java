package com.epam.dmivapi.exception;

public class EntityDoesNotExistException extends RuntimeException {
    public EntityDoesNotExistException(String message) {
        super(message);
    }
}

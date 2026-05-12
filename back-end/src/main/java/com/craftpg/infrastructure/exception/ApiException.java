package com.craftpg.infrastructure.exception;

public class ApiException extends RuntimeException {

    public ApiException(final String message) {
        super(message);
    }
}

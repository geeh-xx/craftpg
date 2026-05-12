package com.craftpg.infrastructure.exception.api;

import com.craftpg.infrastructure.exception.ApiException;

public class InvalidResultException extends ApiException {
    public InvalidResultException(String message) {
        super(message);
    }
}

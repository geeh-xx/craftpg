package com.craftpg.infrastructure.exception;

import org.jspecify.annotations.NonNull;

public class ApiException extends RuntimeException {

    public ApiException(@NonNull final String message) {
        super(message);
    }
}

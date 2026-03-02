package com.craftpg.infrastructure.exception;

import lombok.NonNull;

public class ApiException extends RuntimeException {

    public ApiException(@NonNull final String message) {
        super(message);
    }
}

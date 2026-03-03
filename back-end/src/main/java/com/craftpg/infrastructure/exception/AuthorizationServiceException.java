package com.craftpg.infrastructure.exception;

import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatusCode;

@Getter
public class AuthorizationServiceException extends RuntimeException {

    private final HttpStatusCode statusCode;

    public AuthorizationServiceException(@NonNull final HttpStatusCode statusCode, @NonNull final String message) {
        super(message);
        this.statusCode = statusCode;
    }

}

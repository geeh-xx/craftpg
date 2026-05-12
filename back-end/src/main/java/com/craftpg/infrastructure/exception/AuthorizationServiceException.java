package com.craftpg.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class AuthorizationServiceException extends RuntimeException {

    private final HttpStatusCode statusCode;

    public AuthorizationServiceException(final HttpStatusCode statusCode, final String message) {
        super(message);
        this.statusCode = statusCode;
    }

}

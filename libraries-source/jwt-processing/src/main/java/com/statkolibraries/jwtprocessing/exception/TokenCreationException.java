package com.statkolibraries.jwtprocessing.exception;

public abstract class TokenCreationException extends RuntimeException {
    public TokenCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}

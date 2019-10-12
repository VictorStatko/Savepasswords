package com.statkolibraries.jwtprocessing.exception;

public class TokenProcessingException extends RuntimeException {
    public TokenProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

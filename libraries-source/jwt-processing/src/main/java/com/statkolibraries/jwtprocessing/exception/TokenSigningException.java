package com.statkolibraries.jwtprocessing.exception;

public class TokenSigningException extends TokenCreationException {

    public TokenSigningException(String message, Throwable cause) {
        super(message, cause);
    }
}

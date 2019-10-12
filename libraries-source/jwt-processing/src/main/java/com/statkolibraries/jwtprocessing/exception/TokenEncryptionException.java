package com.statkolibraries.jwtprocessing.exception;

public class TokenEncryptionException extends TokenCreationException {

    public TokenEncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}

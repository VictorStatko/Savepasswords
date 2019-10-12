package com.statkolibraries.jwtprocessing.exception;

public class ExpiredJwtException extends TokenProcessingException {
    public ExpiredJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}

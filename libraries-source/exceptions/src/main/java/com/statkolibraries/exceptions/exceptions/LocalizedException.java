package com.statkolibraries.exceptions.exceptions;

import org.springframework.http.HttpStatus;

public class LocalizedException extends RuntimeException {
    private final String messageKey;
    private final HttpStatus httpStatus;

    public LocalizedException(String description, String messageKey) {
        super(description);
        this.messageKey = messageKey;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public LocalizedException(Throwable exception, String messageKey) {
        super(exception.getMessage(), exception);
        this.messageKey = messageKey;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public LocalizedException(Throwable exception, HttpStatus httpStatus, String messageKey) {
        super(exception.getMessage(), exception);
        this.messageKey = messageKey;
        this.httpStatus = httpStatus;
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

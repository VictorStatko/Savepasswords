package com.statkolibraries.exceptions.exceptions;

public class LocalizedException extends RuntimeException {
    private final String messageKey;

    public LocalizedException(String description, String messageKey) {
        super(description);
        this.messageKey = messageKey;
    }

    public LocalizedException(Throwable exception, String messageKey) {
        super(exception.getMessage(), exception);
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public Throwable getException() {
        return this.getCause();
    }
}

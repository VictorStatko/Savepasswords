package com.statkolibraries.exceptions.domain;

import java.time.Instant;

public abstract class BaseErrorDTO {
    private final String message;
    private final String description;
    private final int status;
    private final Instant timestamp = Instant.now();

    public BaseErrorDTO(String message, String description, int status) {
        this.message = message;
        this.description = description;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public int getStatus() {
        return status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}

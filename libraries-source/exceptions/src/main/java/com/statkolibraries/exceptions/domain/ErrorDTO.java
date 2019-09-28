package com.statkolibraries.exceptions.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ErrorDTO {
    private final String message;
    private final String description;
    private final int status;
    private final Instant timestamp = Instant.now();
    private List<DataErrorDTO> dataErrors;

    public ErrorDTO(String message, String description, int status) {
        this.message = message;
        this.description = description;
        this.status = status;
    }

    public ErrorDTO addDataError(Object descriptor, String message) {
        if (dataErrors == null) {
            dataErrors = new ArrayList<>();
        }
        dataErrors.add(
                new DataErrorDTO(descriptor, message)
        );
        return this;
    }

    public List<DataErrorDTO> getDataErrors() {
        return dataErrors;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDescription() {
        return this.description;
    }

    public int getStatus() {
        return status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}

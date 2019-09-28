package com.statkolibraries.exceptions.domain;

public class DataErrorDTO {

    private final Object descriptor;
    private final String message;

    DataErrorDTO(Object descriptor, String message) {
        this.descriptor = descriptor;
        this.message = message;
    }

    public Object getDescriptor() {
        return descriptor;
    }

    public String getMessage() {
        return message;
    }

}

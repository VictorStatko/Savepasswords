package com.statkolibraries.exceptions.domain;

import java.util.ArrayList;
import java.util.List;

public class ErrorDTO extends BaseErrorDTO {
    private List<DataErrorDTO> dataErrors;

    public ErrorDTO(String message, String description, int status) {
        super(message, description, status);
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

    public ErrorDTO withDataErrors(List<DataErrorDTO> dataErrorList) {
        if (dataErrors == null) {
            dataErrors = new ArrayList<>();
        }
        dataErrors.addAll(dataErrorList);

        return this;
    }

    public List<DataErrorDTO> getDataErrors() {
        return dataErrors;
    }
}

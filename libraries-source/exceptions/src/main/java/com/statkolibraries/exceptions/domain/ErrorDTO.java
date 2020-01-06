package com.statkolibraries.exceptions.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ErrorDTO {
    private Date timestamp = new Date();
    private Integer status;
    private String error;
    private String error_description;
    private List<DataErrorDTO> dataErrors;

    public ErrorDTO(String error, String error_description, int status) {
        this.error = error;
        this.error_description = error_description;
        this.status = status;
    }

    public ErrorDTO() {
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

    public void setDataErrors(List<DataErrorDTO> dataErrors) {
        this.dataErrors = dataErrors;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return error_description;
    }

    public void setErrorDescription(String error_description) {
        this.error_description = error_description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

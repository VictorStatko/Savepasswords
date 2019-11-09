package com.statkolibraries.exceptions.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ErrorDTO {
    private Date timestamp = new Date();
    private Integer status;
    private String message;
    private String description;
    private List<DataErrorDTO> dataErrors;

    public ErrorDTO(String message, String description, int status) {
        this.message = message;
        this.description = description;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

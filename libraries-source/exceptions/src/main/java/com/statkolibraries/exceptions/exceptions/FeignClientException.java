package com.statkolibraries.exceptions.exceptions;

import com.statkolibraries.exceptions.domain.DataErrorDTO;
import org.springframework.http.HttpStatus;

import java.util.List;

public class FeignClientException extends RuntimeException {
    private final String messageKey;
    private final HttpStatus httpStatus;
    private final List<DataErrorDTO> dataErrors;

    public FeignClientException(String description, String messageKey, HttpStatus httpStatus, List<DataErrorDTO> dataErrors) {
        super(description);
        this.messageKey = messageKey;
        this.httpStatus = httpStatus;
        this.dataErrors = dataErrors;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public List<DataErrorDTO> getDataErrors() {
        return dataErrors;
    }

    public Throwable getException() {
        return this.getCause();
    }

}

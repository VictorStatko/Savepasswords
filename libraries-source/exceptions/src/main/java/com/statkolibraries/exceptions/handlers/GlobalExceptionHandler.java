package com.statkolibraries.exceptions.handlers;

import com.statkolibraries.exceptions.domain.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GlobalExceptionHandler {

    private GlobalExceptionHandler(){}

    public static final String REQUEST_ERROR_KEY = "global.requestError";

    public static ResponseEntity<ErrorDTO> processException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(status).body(
                new ErrorDTO(REQUEST_ERROR_KEY, ex.getMessage(), status.value())
        );
    }

}

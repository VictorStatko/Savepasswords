package com.statkolibraries.exceptions.handlers;

import com.statkolibraries.exceptions.domain.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GlobalExceptionHandler {

    public static final String BAD_REQUEST_ERROR_KEY__INTERNAL_SERVER_ERROR = "global.requestError";

    public static ResponseEntity<ErrorDTO> processException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(status).body(
                new ErrorDTO(BAD_REQUEST_ERROR_KEY__INTERNAL_SERVER_ERROR, ex.getMessage(), status.value())
        );
    }

}

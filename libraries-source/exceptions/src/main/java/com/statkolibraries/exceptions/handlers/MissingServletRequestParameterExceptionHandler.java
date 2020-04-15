package com.statkolibraries.exceptions.handlers;

import com.statkolibraries.exceptions.domain.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;

public class MissingServletRequestParameterExceptionHandler {
    private MissingServletRequestParameterExceptionHandler() {
    }

    private static final String BAD_REQUEST_ERROR_KEY__OBJECT_VALIDATION_ERROR = "global.serverValidationError";

    public static ResponseEntity<ErrorDTO> processMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        final ErrorDTO dto = new ErrorDTO(
                BAD_REQUEST_ERROR_KEY__OBJECT_VALIDATION_ERROR, ex.getMessage(), status.value()
        );

        return ResponseEntity.status(status).body(dto);
    }
}

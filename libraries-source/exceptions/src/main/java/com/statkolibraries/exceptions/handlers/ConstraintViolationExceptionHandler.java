package com.statkolibraries.exceptions.handlers;

import com.statkolibraries.exceptions.domain.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class ConstraintViolationExceptionHandler {

    private ConstraintViolationExceptionHandler() {
    }

    private static final String BAD_REQUEST_ERROR_KEY__OBJECT_VALIDATION_ERROR = "global.serverValidationError";

    public static ResponseEntity<ErrorDTO> processConstraintViolationException(ConstraintViolationException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        final ErrorDTO dto = new ErrorDTO(
                BAD_REQUEST_ERROR_KEY__OBJECT_VALIDATION_ERROR, ex.getLocalizedMessage(), status.value()
        );

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            dto.addDataError(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath(), violation.getMessage());
        }

        return ResponseEntity.status(status).body(dto);
    }
}


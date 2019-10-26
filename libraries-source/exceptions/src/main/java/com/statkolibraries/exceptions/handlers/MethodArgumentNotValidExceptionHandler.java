package com.statkolibraries.exceptions.handlers;

import com.statkolibraries.exceptions.domain.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class MethodArgumentNotValidExceptionHandler {

    private MethodArgumentNotValidExceptionHandler() {
    }

    private static final String BAD_REQUEST_ERROR_KEY__OBJECT_VALIDATION_ERROR = "global.serverValidationError";

    public static ResponseEntity<ErrorDTO> processValidationError(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        final ErrorDTO dto = new ErrorDTO(
                BAD_REQUEST_ERROR_KEY__OBJECT_VALIDATION_ERROR, "Error during object validation.", status.value()
        );

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            dto.addDataError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(status).body(dto);
    }
}

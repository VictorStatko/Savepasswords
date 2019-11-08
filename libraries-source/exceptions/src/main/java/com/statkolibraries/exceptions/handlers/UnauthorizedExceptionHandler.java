package com.statkolibraries.exceptions.handlers;

import com.statkolibraries.exceptions.domain.ErrorDTO;
import com.statkolibraries.exceptions.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UnauthorizedExceptionHandler {

    public static final String UNAUTHORIZED_ERROR_KEY__UNAUTHORIZED_ERROR = "global.unauthorized";

    private UnauthorizedExceptionHandler() {
    }

    public static ResponseEntity<ErrorDTO> processUnauthorizedException(UnauthorizedException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        return ResponseEntity.status(status).body(
                new ErrorDTO(UNAUTHORIZED_ERROR_KEY__UNAUTHORIZED_ERROR, ex.getMessage(), status.value())
        );
    }

}

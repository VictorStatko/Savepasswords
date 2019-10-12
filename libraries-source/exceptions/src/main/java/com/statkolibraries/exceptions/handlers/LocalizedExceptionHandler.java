package com.statkolibraries.exceptions.handlers;

import com.statkolibraries.exceptions.domain.ErrorDTO;
import com.statkolibraries.exceptions.exceptions.LocalizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class LocalizedExceptionHandler {
    private LocalizedExceptionHandler() {
    }

    public static ResponseEntity<ErrorDTO> processLocalizedException(LocalizedException ex) {
        HttpStatus status = ex.getHttpStatus();

        return ResponseEntity.status(status).body(
                new ErrorDTO(ex.getMessageKey(), ex.getMessage(), status.value())
        );
    }
}

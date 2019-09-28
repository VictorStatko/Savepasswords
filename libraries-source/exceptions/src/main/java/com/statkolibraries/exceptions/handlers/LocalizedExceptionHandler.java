package com.statkolibraries.exceptions.handlers;

import com.statkolibraries.exceptions.domain.ErrorDTO;
import com.statkolibraries.exceptions.exceptions.LocalizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;

public class LocalizedExceptionHandler {
    private LocalizedExceptionHandler() {
    }

    public static ResponseEntity<ErrorDTO> processLocalizedException(LocalizedException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (ex.getException() instanceof EntityNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(status).body(new ErrorDTO(ex.getMessageKey(), ex.getMessage()));
    }
}

package com.statkovit.userservice.exceptions;

import com.statkolibraries.exceptions.domain.ErrorDTO;
import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkolibraries.exceptions.handlers.LocalizedExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler(LocalizedException.class)
    public ResponseEntity<ErrorDTO> processLocalizedException(LocalizedException ex) {
        return LocalizedExceptionHandler.processLocalizedException(ex);
    }

}

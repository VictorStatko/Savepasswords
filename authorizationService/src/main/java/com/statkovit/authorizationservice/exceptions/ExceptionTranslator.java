package com.statkovit.authorizationservice.exceptions;

import com.statkolibraries.exceptions.domain.ErrorDTO;
import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkolibraries.exceptions.exceptions.UnauthorizedException;
import com.statkolibraries.exceptions.handlers.GlobalExceptionHandler;
import com.statkolibraries.exceptions.handlers.LocalizedExceptionHandler;
import com.statkolibraries.exceptions.handlers.UnauthorizedExceptionHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class ExceptionTranslator {

    @ExceptionHandler(LocalizedException.class)
    public ResponseEntity<ErrorDTO> processLocalizedException(LocalizedException ex) {
        log.error(ex.getMessage(), ex);
        return LocalizedExceptionHandler.processLocalizedException(ex);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDTO> processUnauthorizedException(UnauthorizedException ex) {
        log.error(ex.getMessage(), ex);
        return UnauthorizedExceptionHandler.processUnauthorizedException(ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> processGlobalException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return GlobalExceptionHandler.processException(ex);
    }

}
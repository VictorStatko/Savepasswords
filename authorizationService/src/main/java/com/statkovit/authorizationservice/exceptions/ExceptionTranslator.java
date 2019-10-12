package com.statkovit.authorizationservice.exceptions;

import com.statkolibraries.exceptions.domain.ErrorDTO;
import com.statkolibraries.exceptions.exceptions.FeignClientException;
import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkolibraries.exceptions.handlers.FeignExceptionHandler;
import com.statkolibraries.exceptions.handlers.GlobalExceptionHandler;
import com.statkolibraries.exceptions.handlers.LocalizedExceptionHandler;
import com.statkolibraries.exceptions.handlers.MethodArgumentNotValidExceptionHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class ExceptionTranslator {

    @ExceptionHandler(LocalizedException.class)
    public ResponseEntity<ErrorDTO> processLocalizedException(LocalizedException ex) {
        log.error(ex.getMessage(), ex.getException());
        return LocalizedExceptionHandler.processLocalizedException(ex);
    }

    @ExceptionHandler(FeignClientException.class)
    public ResponseEntity<ErrorDTO> processFeignClientException(FeignClientException ex) {
        log.error(ex.getMessage(), ex.getException());
        return FeignExceptionHandler.processFeignClientException(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> processMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex.getCause());
        return MethodArgumentNotValidExceptionHandler.processValidationError(ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> processGlobalException(Exception ex) {
        log.error(ex.getMessage(), ex.getCause());
        return GlobalExceptionHandler.processException(ex);
    }

}
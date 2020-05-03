package com.statkovit.authorizationservice.exceptions.translators;

import com.statkolibraries.exceptions.domain.ErrorDTO;
import com.statkolibraries.exceptions.exceptions.FeignClientException;
import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkolibraries.exceptions.handlers.FeignExceptionHandler;
import com.statkolibraries.exceptions.handlers.GlobalExceptionHandler;
import com.statkolibraries.exceptions.handlers.LocalizedExceptionHandler;
import com.statkolibraries.exceptions.handlers.MethodArgumentNotValidExceptionHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> processMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        return MethodArgumentNotValidExceptionHandler.processValidationError(ex);
    }

    @ExceptionHandler(FeignClientException.class)
    public ResponseEntity<ErrorDTO> processFeignClientException(FeignClientException ex) {
        log.error(ex.getMessage(), ex);
        return FeignExceptionHandler.processFeignClientException(ex);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ErrorDTO> processTransactionSystemException(Exception ex) {
        final Throwable cause = ((TransactionSystemException) ex).getRootCause();

        if (cause instanceof LocalizedException) {
            return processLocalizedException((LocalizedException) cause);
        }

        if (cause instanceof FeignClientException) {
            return processFeignClientException((FeignClientException) cause);
        }

        return processGlobalException(ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> processGlobalException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return GlobalExceptionHandler.processException(ex);
    }

}
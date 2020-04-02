package com.statkovit.personalAccountsService.unit.exceptions;

import com.statkolibraries.exceptions.domain.ErrorDTO;
import com.statkolibraries.exceptions.exceptions.FeignClientException;
import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.exceptions.ExceptionTranslator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ExceptionTranslatorUTest {

    @InjectMocks
    private ExceptionTranslator exceptionTranslator;

    @Test
    void processLocalizedExceptionShouldReturnResponseEntityWithErrorDTO() {
        LocalizedException exception = new LocalizedException("test", "test");

        ResponseEntity<ErrorDTO> responseEntity = exceptionTranslator.processLocalizedException(exception);

        Assertions.assertNotNull(responseEntity);
        ErrorDTO errorDTO = responseEntity.getBody();
        Assertions.assertNotNull(errorDTO);
    }

    @Test
    void processMethodArgumentNotValidExceptionShouldReturnResponseEntityWithErrorDTO() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(new FieldError("qwerty", "field", "should not be empty")));
        when(exception.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ErrorDTO> responseEntity = exceptionTranslator.processMethodArgumentNotValidException(exception);

        Assertions.assertNotNull(responseEntity);
        ErrorDTO errorDTO = responseEntity.getBody();
        Assertions.assertNotNull(errorDTO);
    }

    @Test
    void processFeignClientExceptionShouldReturnResponseEntityWithErrorDTO() {
        FeignClientException exception = new FeignClientException(
                "description", "key", HttpStatus.BAD_REQUEST, Collections.emptyList()
        );

        ResponseEntity<ErrorDTO> responseEntity = exceptionTranslator.processFeignClientException(exception);

        Assertions.assertNotNull(responseEntity);
        ErrorDTO errorDTO = responseEntity.getBody();
        Assertions.assertNotNull(errorDTO);
    }

    @Test
    void processGlobalExceptionShouldReturnResponseEntityWithErrorDTO() {
        RuntimeException exception = new RuntimeException("custom");

        ResponseEntity<ErrorDTO> responseEntity = exceptionTranslator.processGlobalException(exception);

        Assertions.assertNotNull(responseEntity);
        ErrorDTO errorDTO = responseEntity.getBody();
        Assertions.assertNotNull(errorDTO);
    }
}
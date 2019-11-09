package com.statkolibraries.exceptions.handlers;

import com.statkolibraries.exceptions.domain.ErrorDTO;
import com.statkolibraries.exceptions.exceptions.FeignClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

public class FeignExceptionHandler {
    public static ResponseEntity<ErrorDTO> processFeignClientException(FeignClientException ex) {
        HttpStatus status = ex.getHttpStatus();

        final ErrorDTO errorDTO = new ErrorDTO(ex.getMessageKey(), ex.getMessage(), status.value());

        if (!CollectionUtils.isEmpty(ex.getDataErrors())) {
            errorDTO.setDataErrors(ex.getDataErrors());
        }

        return ResponseEntity.status(status).body(errorDTO);
    }
}

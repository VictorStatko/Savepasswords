package com.statkolibraries.exceptions.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.statkolibraries.exceptions.domain.ErrorDTO;
import com.statkolibraries.exceptions.exceptions.FeignClientException;
import com.statkolibraries.exceptions.handlers.GlobalExceptionHandler;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;

import java.io.Reader;
import java.util.Objects;

public class FeignRequestErrorDecoder implements ErrorDecoder {

    private ObjectMapper objectMapper;

    public FeignRequestErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String s, Response response) {
        if (Objects.isNull(response.body())) {
            return new FeignClientException(
                    "Error during executing feign request (empty response body)", GlobalExceptionHandler.REQUEST_ERROR_KEY, HttpStatus.valueOf(response.status()), null
            );
        }

        try (Reader reader = response.body().asReader()) {
            String result = IOUtils.toString(reader);
            ErrorDTO errorDTO = objectMapper.readValue(result, ErrorDTO.class);
            return new FeignClientException(
                    errorDTO.getDescription(), errorDTO.getMessage(), HttpStatus.valueOf(response.status()), errorDTO.getDataErrors()
            );
        } catch (Exception e) {
            return new FeignClientException(
                    "Feign exception processing error",
                    GlobalExceptionHandler.REQUEST_ERROR_KEY,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
    }
}

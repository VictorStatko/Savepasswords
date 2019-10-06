package com.statkolibraries.exceptions.feign;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.statkolibraries.exceptions.domain.DataErrorDTO;
import com.statkolibraries.exceptions.exceptions.FeignClientException;
import com.statkolibraries.exceptions.handlers.GlobalExceptionHandler;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class FeignRequestErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        String message;
        String description;
        List<DataErrorDTO> dataErrors = new ArrayList<>();
        try (Reader reader = response.body().asReader()) {
            String result = IOUtils.toString(reader);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonFactory factory = objectMapper.getFactory();
            JsonParser jsonParser = factory.createParser(result);
            JsonNode jsonNode = objectMapper.readTree(jsonParser);
            message = jsonNode.get("message").asText();
            description = jsonNode.get("description").asText();
            JsonNode errorsList = jsonNode.get("dataErrors");
            for (JsonNode error : errorsList) {
                String descriptor = error.get("descriptor").asText();
                String errorMessage = error.get("message").asText();
                dataErrors.add(new DataErrorDTO(descriptor, errorMessage));
            }
        } catch (Exception e) {
            return new FeignClientException(
                    "Feign response processing error",
                    GlobalExceptionHandler.BAD_REQUEST_ERROR_KEY__INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
        return new FeignClientException(
                description, message, HttpStatus.valueOf(response.status()), dataErrors
        );
    }
}

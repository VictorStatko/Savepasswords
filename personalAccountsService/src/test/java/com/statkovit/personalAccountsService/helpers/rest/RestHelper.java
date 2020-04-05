package com.statkovit.personalAccountsService.helpers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Objects;

public final class RestHelper {

    private RestHelper() {
    }

    private static <T> HttpEntity<T> prepareHttpEntityForRequest(T body, Map<String, String> headersForAdd) {
        final HttpHeaders httpHeaders = new HttpHeaders();

        if (MapUtils.isNotEmpty(headersForAdd)) {
            headersForAdd.forEach(httpHeaders::set);
        }

        return new HttpEntity<T>(body, httpHeaders);
    }

    @SneakyThrows
    private static <T> T convertJsonStringToObject(String jsonString, Class<T> tClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, tClass);
    }

    @SneakyThrows
    public static <T> HttpResponse<T> sendRequest(TestRestTemplate restTemplate, String route, HttpMethod method,
                                                  Object body, Map<String, String> headers, Class<T> tClass) {
        printDelimiter();

        final ObjectMapper objectMapper = new ObjectMapper();
        final ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

        HttpEntity<?> httpEntity = RestHelper.prepareHttpEntityForRequest(body, headers);

        System.out.println(String.format("Request - %s %s", method, route));
        System.out.println(objectWriter.writeValueAsString(httpEntity));
        System.out.println();

        ResponseEntity<String> response = restTemplate.exchange(route, method, httpEntity, String.class);

        System.out.println(String.format("Response - %s", response.getStatusCode()));

        if (StringUtils.isEmpty(response.getBody())) {
            System.out.println("Empty response body.");
        } else {
            System.out.println(
                    objectWriter.writeValueAsString(
                            objectMapper.readValue(response.getBody(), Object.class)
                    )
            );
        }

        T convertedObject;

        if (response.getStatusCode().is2xxSuccessful() && tClass != Void.class && Objects.nonNull(response.getBody())) {
            convertedObject = convertJsonStringToObject(response.getBody(), tClass);
        } else {
            convertedObject = null;
        }

        printDelimiter();

        return new HttpResponse<>(convertedObject, response);
    }

    public static HttpResponse<Void> sendRequest(TestRestTemplate restTemplate, String route, HttpMethod method, Map<String, String> headers) {
        return sendRequest(restTemplate, route, method, null, headers, Void.class);
    }

    public static <T> HttpResponse<T> sendRequest(TestRestTemplate restTemplate, String route, HttpMethod method, Map<String, String> headers, Class<T> tClass) {
        return sendRequest(restTemplate, route, method, null, headers, tClass);
    }

    public static <T> HttpResponse<T> sendRequest(TestRestTemplate restTemplate, String route, HttpMethod method, Object body, Class<T> tClass) {
        return sendRequest(restTemplate, route, method, body, null, tClass);
    }

    private static void printDelimiter() {
        System.out.println("-".repeat(200));
    }

    public static class HttpResponse<T> {
        private final T convertedResponse;
        private final ResponseEntity<String> responseEntity;

        public HttpResponse(T convertedResponse, ResponseEntity<String> responseEntity) {
            this.convertedResponse = convertedResponse;
            this.responseEntity = responseEntity;
        }

        public T getConvertedResponse() {
            return convertedResponse;
        }

        public ResponseEntity<String> getResponseEntity() {
            return responseEntity;
        }
    }
}

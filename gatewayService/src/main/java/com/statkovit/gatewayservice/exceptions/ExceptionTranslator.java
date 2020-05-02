package com.statkovit.gatewayservice.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.statkolibraries.exceptions.domain.ErrorDTO;
import com.statkolibraries.exceptions.handlers.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Order(-2)
@SuppressWarnings("NullableProblems")
@RequiredArgsConstructor
@Log4j2
public class ExceptionTranslator implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error(ex.getMessage(), ex);

        ResponseEntity<ErrorDTO> responseEntity = GlobalExceptionHandler.processException((Exception) ex);

        byte[] bytes = new byte[0];

        if (Objects.nonNull(responseEntity.getBody())) {
            try {
                bytes = objectMapper.writeValueAsBytes(responseEntity.getBody());
            } catch (JsonProcessingException e) {
                bytes = e.getMessage().getBytes();
            }
        }

        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
        exchange.getResponse().setStatusCode(responseEntity.getStatusCode());
        DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(dataBuffer));
    }
}

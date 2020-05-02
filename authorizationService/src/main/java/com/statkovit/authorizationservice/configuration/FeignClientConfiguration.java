package com.statkovit.authorizationservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.statkolibraries.exceptions.feign.FeignRequestErrorDecoder;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FeignClientConfiguration {

    private final ObjectMapper objectMapper;

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignRequestErrorDecoder(objectMapper);
    }
}

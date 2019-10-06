package com.statkovit.authorizationservice.configuration;

import com.statkolibraries.exceptions.feign.FeignRequestErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignRequestErrorDecoder();
    }
}

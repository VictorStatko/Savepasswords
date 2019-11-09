package com.statkovit.gatewayservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "auth-service")
public interface AuthServiceRestClient {

    String EXCHANGE_URL = "/internal-api/v1/exchange";

    @RequestMapping(method = RequestMethod.POST, value = EXCHANGE_URL)
    ResponseEntity<Void> exchangeAuthToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);
}

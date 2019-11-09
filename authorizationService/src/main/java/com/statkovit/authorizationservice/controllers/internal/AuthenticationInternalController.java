package com.statkovit.authorizationservice.controllers.internal;

import com.statkovit.authorizationservice.constants.ServerConstants;
import com.statkovit.authorizationservice.rest.AuthenticationRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
public class AuthenticationInternalController {

    private final AuthenticationRestService authenticationRestService;

    @PostMapping(ServerConstants.API_V1_INTERNAL_ENDPOINT + "exchange")
    public void exchangeTokens(@RequestHeader(AUTHORIZATION) String authHeader, HttpServletResponse response) {
        authenticationRestService.exchange(authHeader, response);
    }

}

package com.statkovit.authorizationservice.controllers;

import com.statkovit.authorizationservice.constants.ServerConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private static final String CONTROLLER_ENDPOINT = ServerConstants.API_V1_ENDPOINT + "authentication";

    @GetMapping(CONTROLLER_ENDPOINT + "/test")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}

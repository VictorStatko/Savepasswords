package com.statkovit.userservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.statkovit.userservice.constants.ServerConstants.SERVER_ENDPOINT;

@RestController
public class RegistrationController {

    @GetMapping(SERVER_ENDPOINT + "/test")
    public String index() {
        return "Greetings from Registration controller!";
    }
}

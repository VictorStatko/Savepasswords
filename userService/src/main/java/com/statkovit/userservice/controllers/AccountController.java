package com.statkovit.userservice.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.statkovit.userservice.constants.ServerConstants.SERVER_ENDPOINT;

@RestController
public class AccountController {

    @PostMapping(SERVER_ENDPOINT + "/sign-up")
    public String index() {
        return "Greetings from Registration controller!";
    }
}

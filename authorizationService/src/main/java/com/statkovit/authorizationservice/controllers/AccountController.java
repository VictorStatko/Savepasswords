package com.statkovit.authorizationservice.controllers;

import com.statkovit.authorizationservice.constants.ServerConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AccountController {

    private static final String CONTROLLER_ROUTE = ServerConstants.API_ROUTE + "accounts";

    @GetMapping(CONTROLLER_ROUTE + "/current")
    public Principal getUser(Principal principal) {
        return principal;
    }

}

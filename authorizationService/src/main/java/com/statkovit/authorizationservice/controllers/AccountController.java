package com.statkovit.authorizationservice.controllers;

import com.statkovit.authorizationservice.constants.ServerConstants;
import com.statkovit.authorizationservice.payload.AccountDto;
import com.statkovit.authorizationservice.rest.AccountsRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private static final String CONTROLLER_ROUTE = ServerConstants.API_ROUTE + "accounts";

    private final AccountsRestService accountsRestService;


    @GetMapping(CONTROLLER_ROUTE + "/current")
    public Principal getUser(Principal principal) {
        return principal;
    }

    @PostMapping(CONTROLLER_ROUTE)
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody AccountDto accountDto) {
        AccountDto dto = accountsRestService.create(accountDto);
        return ResponseEntity.ok(dto);
    }

}

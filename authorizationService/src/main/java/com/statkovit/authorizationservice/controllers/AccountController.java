package com.statkovit.authorizationservice.controllers;

import com.statkovit.authorizationservice.constants.ServerConstants;
import com.statkovit.authorizationservice.payload.AccountDto;
import com.statkovit.authorizationservice.payload.ExtendedAccountDto;
import com.statkovit.authorizationservice.rest.AccountsRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private static final String CONTROLLER_ROUTE = ServerConstants.AUTH_API_ROUTE + "accounts";

    private final AccountsRestService accountsRestService;

    @PostMapping(CONTROLLER_ROUTE)
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody AccountDto accountDto) {
        AccountDto dto = accountsRestService.create(accountDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(CONTROLLER_ROUTE + "/current")
    public ResponseEntity<ExtendedAccountDto> getCurrentAccount() {
        ExtendedAccountDto dto = accountsRestService.getCurrentAccountDataFromAuth();
        return ResponseEntity.ok(dto);
    }

}

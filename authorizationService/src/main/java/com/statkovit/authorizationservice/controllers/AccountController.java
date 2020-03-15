package com.statkovit.authorizationservice.controllers;

import com.statkovit.authorizationservice.constants.ServerConstants;
import com.statkovit.authorizationservice.payload.AccountDto;
import com.statkovit.authorizationservice.payload.ExtendedAccountDto;
import com.statkovit.authorizationservice.payload.StringDto;
import com.statkovit.authorizationservice.rest.AccountsRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private static final String CONTROLLER_ROUTE = ServerConstants.AUTH_API_ROUTE + "accounts";

    private final AccountsRestService accountsRestService;
    private final PasswordEncoder passwordEncoder;

    //TODO temporary route for internal use, will be removed
    @GetMapping(CONTROLLER_ROUTE + "/argon")
    public ResponseEntity<String> encryptString(@Valid @RequestParam String key) {
        return ResponseEntity.ok(passwordEncoder.encode(key));
    }

    @GetMapping(CONTROLLER_ROUTE + "/client-encryption-salt")
    public ResponseEntity<StringDto> getClientSalt(@Valid @RequestParam String email) {
        StringDto dto = accountsRestService.getClientEncryptionSalt(email);
        return ResponseEntity.ok(dto);
    }

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

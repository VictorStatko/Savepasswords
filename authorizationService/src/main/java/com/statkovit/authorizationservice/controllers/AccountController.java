package com.statkovit.authorizationservice.controllers;

import com.statkovit.authorizationservice.constants.ServerConstants;
import com.statkovit.authorizationservice.domainServices.AccountsDomainService;
import com.statkovit.authorizationservice.payload.AccountDto;
import com.statkovit.authorizationservice.payload.KeyPairDto;
import com.statkovit.authorizationservice.payload.StringDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private static final String CONTROLLER_ROUTE = ServerConstants.AUTH_API_ROUTE + "accounts";

    private final AccountsDomainService accountsDomainService;

    @GetMapping(CONTROLLER_ROUTE + "/client-encryption-salt")
    public ResponseEntity<StringDto> getClientSalt(@Valid @RequestParam String email) {
        StringDto dto = accountsDomainService.getClientEncryptionSalt(email);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(CONTROLLER_ROUTE)
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody AccountDto accountDto) {
        AccountDto dto = accountsDomainService.create(accountDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(CONTROLLER_ROUTE + "/current")
    public ResponseEntity<AccountDto> getCurrentAccount() {
        AccountDto dto = accountsDomainService.getCurrentAccountData();
        return ResponseEntity.ok(dto);
    }

    @GetMapping(CONTROLLER_ROUTE + "/current/keypair")
    public ResponseEntity<KeyPairDto> getCurrentAccountKeypair() {
        KeyPairDto dto = accountsDomainService.getCurrentAccountKeypair();
        return ResponseEntity.ok(dto);
    }

}

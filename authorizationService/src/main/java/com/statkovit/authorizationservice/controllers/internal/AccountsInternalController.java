package com.statkovit.authorizationservice.controllers.internal;

import com.statkovit.authorizationservice.constants.ServerConstants;
import com.statkovit.authorizationservice.payload.AccountDto;
import com.statkovit.authorizationservice.rest.AccountsRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccountsInternalController {

    private static final String CONTROLLER_ROUTE = ServerConstants.INTERNAL_API_ROUTE + "accounts";

    private final AccountsRestService accountsRestService;

    @PostMapping(CONTROLLER_ROUTE)
    @PreAuthorize("#oauth2.hasScope('service')")
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody AccountDto accountDto) {
        AccountDto dto = accountsRestService.create(accountDto);
        return ResponseEntity.ok(dto);
    }
}

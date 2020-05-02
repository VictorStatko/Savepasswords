package com.statkovit.authorizationservice.controllers.internal;

import com.statkovit.authorizationservice.constants.ServerConstants;
import com.statkovit.authorizationservice.domainServices.AccountsDomainService;
import com.statkovit.authorizationservice.payload.ExtendedAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountsInternalController {

    private static final String CONTROLLER_ROUTE = ServerConstants.INTERNAL_API_ROUTE + "accounts";
    private final AccountsDomainService accountsDomainService;

    //TODO
    // @PostMapping(CONTROLLER_ROUTE)
    //@PreAuthorize("#oauth2.hasScope('service')")

    @GetMapping(CONTROLLER_ROUTE + "/current")
    public ResponseEntity<ExtendedAccountDto> getCurrentAccount() {
        ExtendedAccountDto dto = accountsDomainService.getCurrentExtendedAccountData();
        return ResponseEntity.ok(dto);
    }
}

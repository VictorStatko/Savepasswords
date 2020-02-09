package com.statkovit.authorizationservice.controllers.internal;

import com.statkovit.authorizationservice.constants.ServerConstants;
import com.statkovit.authorizationservice.rest.AccountsRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountsInternalController {

    private static final String CONTROLLER_ROUTE = ServerConstants.INTERNAL_API_ROUTE + "accounts";

    private final AccountsRestService accountsRestService;

    // @PostMapping(CONTROLLER_ROUTE)
    //@PreAuthorize("#oauth2.hasScope('service')")

}

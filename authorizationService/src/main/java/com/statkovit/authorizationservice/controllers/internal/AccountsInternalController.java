package com.statkovit.authorizationservice.controllers.internal;

import com.statkovit.authorizationservice.constants.ServerConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AccountsInternalController {

    private static final String CONTROLLER_ROUTE = ServerConstants.INTERNAL_API_ROUTE + "accounts";

    // @PostMapping(CONTROLLER_ROUTE)
    //@PreAuthorize("#oauth2.hasScope('service')")

    @GetMapping(CONTROLLER_ROUTE + "/current")
    public Principal getUser(Principal principal) {
        return principal;
    }
}

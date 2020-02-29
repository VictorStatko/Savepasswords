package com.statkovit.personalAccountsService.controllers.external;

import com.statkovit.personalAccountsService.constants.ServerConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PasswordsController {

    private static final String CONTROLLER_ROUTE = ServerConstants.EXTERNAL_API_ROUTE + "passwords";

    @GetMapping(CONTROLLER_ROUTE)
    public ResponseEntity<String> getCurrentAccount() {
        return ResponseEntity.ok("Ok!");
    }
}

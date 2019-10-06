package com.statkovit.userservice.controllers.internal;

import com.statkovit.userservice.constants.ServerConstants;
import com.statkovit.userservice.dto.AccountLoginDTO;
import com.statkovit.userservice.dto.CredentialsDTO;
import com.statkovit.userservice.rest.AccountRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccountInternalController {

    private static final String CONTROLLER_ENDPOINT = ServerConstants.INTERNAL_API_V1_ENDPOINT + "accounts";

    private final AccountRestService accountRestService;

    @PostMapping(CONTROLLER_ENDPOINT + "/request-login")
    public ResponseEntity requestLogin(@Valid @RequestBody CredentialsDTO credentialsDTO) {
        AccountLoginDTO accountLoginDTO = accountRestService.requestLogin(credentialsDTO);

        return ResponseEntity.ok(accountLoginDTO);
    }
}

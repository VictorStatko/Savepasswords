package com.statkovit.userservice.controllers.internal;

import com.statkovit.userservice.constants.ServerConstants;
import com.statkovit.userservice.dto.AccountDataDTO;
import com.statkovit.userservice.dto.CredentialsDTO;
import com.statkovit.userservice.rest.AccountRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountInternalController {

    private static final String CONTROLLER_ENDPOINT = ServerConstants.INTERNAL_API_V1_ENDPOINT + "accounts";

    private final AccountRestService accountRestService;

    @PostMapping(CONTROLLER_ENDPOINT + "/request-login")
    public ResponseEntity requestLogin(@Valid @RequestBody CredentialsDTO credentialsDTO) {
        AccountDataDTO accountDataDTO = accountRestService.requestLogin(credentialsDTO);

        return ResponseEntity.ok(accountDataDTO);
    }

    //TODO security
    @GetMapping(CONTROLLER_ENDPOINT + "/{uuid}")
    public ResponseEntity requestLogin(@PathVariable(name = "uuid") UUID uuid) {
        AccountDataDTO accountDataDTO = accountRestService.getAccountData(uuid);

        return ResponseEntity.ok(accountDataDTO);
    }
}

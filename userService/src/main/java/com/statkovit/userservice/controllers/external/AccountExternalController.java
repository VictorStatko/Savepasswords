package com.statkovit.userservice.controllers.external;

import com.statkovit.userservice.constants.ServerConstants;
import com.statkovit.userservice.dto.AccountExistsDTO;
import com.statkovit.userservice.dto.BooleanDTO;
import com.statkovit.userservice.dto.SignUpDTO;
import com.statkovit.userservice.rest.AccountRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccountExternalController {

    private static final String CONTROLLER_ENDPOINT = ServerConstants.EXTERNAL_API_V1_ENDPOINT + "accounts";

    private final AccountRestService accountRestService;

    @PostMapping(CONTROLLER_ENDPOINT + "/sign-up")
    public void signUp(@Valid @RequestBody SignUpDTO signUpDTO) {
        accountRestService.signUp(signUpDTO);
    }

    @PostMapping(CONTROLLER_ENDPOINT + "/exists")
    public ResponseEntity checkAccountAlreadyExists(@Valid @RequestBody AccountExistsDTO accountExistsDTO) {
        BooleanDTO exists = accountRestService.isAccountExists(accountExistsDTO);
        return ResponseEntity.ok(exists);
    }
}

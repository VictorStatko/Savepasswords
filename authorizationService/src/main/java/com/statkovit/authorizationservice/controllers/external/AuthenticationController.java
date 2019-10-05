package com.statkovit.authorizationservice.controllers.external;

import com.statkovit.authorizationservice.constants.ServerConstants;
import com.statkovit.authorizationservice.payloads.SignInDTO;
import com.statkovit.authorizationservice.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(ServerConstants.API_V1_EXTERNAL_ENDPOINT + "sign-in")
    public ResponseEntity signIn(@RequestBody @Valid SignInDTO signInDTO) {
        String token = authenticationService.signIn(signInDTO);
        return ResponseEntity.ok(token);
    }
}

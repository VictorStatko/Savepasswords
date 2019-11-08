package com.statkovit.authorizationservice.controllers.external;

import com.statkovit.authorizationservice.constants.ServerConstants;
import com.statkovit.authorizationservice.payloads.SignInDTO;
import com.statkovit.authorizationservice.rest.AuthenticationRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private static final String HEADER_REFRESH_TOKEN = "Refresh-Token";
    private final AuthenticationRestService authenticationRestService;

    @PostMapping(ServerConstants.API_V1_EXTERNAL_ENDPOINT + "sign-in")
    public void signIn(@RequestBody @Valid SignInDTO signInDTO, HttpServletResponse response) {
        authenticationRestService.signIn(signInDTO, response);
    }

    @PostMapping(ServerConstants.API_V1_EXTERNAL_ENDPOINT + "refresh")
    public void refreshToken(@RequestHeader(HEADER_REFRESH_TOKEN) String refreshToken, HttpServletResponse response) {
    }
}

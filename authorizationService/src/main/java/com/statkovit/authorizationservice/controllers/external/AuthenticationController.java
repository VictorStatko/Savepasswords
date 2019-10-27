package com.statkovit.authorizationservice.controllers.external;

import com.statkovit.authorizationservice.constants.ServerConstants;
import com.statkovit.authorizationservice.payloads.SignInDTO;
import com.statkovit.authorizationservice.services.AuthenticationService;
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
    private static final String EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    private final AuthenticationService authenticationService;

    @PostMapping(ServerConstants.API_V1_EXTERNAL_ENDPOINT + "sign-in")
    public void signIn(@RequestBody @Valid SignInDTO signInDTO, HttpServletResponse response) {
        String refreshToken = authenticationService.signIn(signInDTO, response);
        response.addHeader(EXPOSE_HEADERS, HEADER_REFRESH_TOKEN);
        response.addHeader(HEADER_REFRESH_TOKEN, refreshToken);
    }

    @PostMapping(ServerConstants.API_V1_EXTERNAL_ENDPOINT + "refresh")
    public void refreshToken(@RequestHeader(HEADER_REFRESH_TOKEN) String refreshToken, HttpServletResponse response) {
        authenticationService.refresh(refreshToken, response);
    }
}

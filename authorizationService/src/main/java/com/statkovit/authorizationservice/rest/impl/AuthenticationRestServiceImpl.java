package com.statkovit.authorizationservice.rest.impl;

import com.statkovit.authorizationservice.payloads.SignInDTO;
import com.statkovit.authorizationservice.rest.AuthenticationRestService;
import com.statkovit.authorizationservice.services.AuthenticationService;
import com.statkovit.authorizationservice.services.CookieService;
import com.statkovit.authorizationservice.services.transfer.TokensCreationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationRestServiceImpl implements AuthenticationRestService {
    private static final String AUTHORIZATION_COOKIE = "Authorization";
    private static final String HEADER_AUTHORIZATION_EXPIRATION = "Authorization-Expired-At";
    private static final String HEADER_REFRESH_TOKEN = "Refresh-Token";
    private static final String EXPOSE_HEADERS = "Access-Control-Expose-Headers";

    private final AuthenticationService authenticationService;
    private final CookieService cookieService;

    @Override
    public void signIn(SignInDTO signInDTO, HttpServletResponse httpServletResponse) {
        TokensCreationResult result = authenticationService.signIn(signInDTO);

        cookieService.addCookies(
                Collections.singletonMap(AUTHORIZATION_COOKIE, result.getAccessToken()), httpServletResponse
        );

        httpServletResponse.addHeader(HEADER_REFRESH_TOKEN, result.getRefreshToken());
        httpServletResponse.addHeader(HEADER_AUTHORIZATION_EXPIRATION, result.getAccessTokenExpiration());
        httpServletResponse.addHeader(EXPOSE_HEADERS, String.join(", ", HEADER_REFRESH_TOKEN, HEADER_AUTHORIZATION_EXPIRATION));
    }
}

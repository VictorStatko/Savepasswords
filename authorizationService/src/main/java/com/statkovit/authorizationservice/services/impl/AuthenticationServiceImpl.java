package com.statkovit.authorizationservice.services.impl;

import com.statkolibraries.jwtprocessing.JwsCreator;
import com.statkolibraries.jwtprocessing.JwsProcessor;
import com.statkolibraries.jwtprocessing.payload.TokenData;
import com.statkovit.authorizationservice.feign.UserServiceRestClient;
import com.statkovit.authorizationservice.payloads.AccountDTO;
import com.statkovit.authorizationservice.payloads.SignInDTO;
import com.statkovit.authorizationservice.properties.JwtProperties;
import com.statkovit.authorizationservice.services.AuthenticationService;
import com.statkovit.authorizationservice.services.CookieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String AUTHORIZATION_COOKIE = "Authorization";

    private final UserServiceRestClient userServiceRestClient;
    private final JwtProperties jwtProperties;
    private final CookieService cookieService;

    /**
     * This method create new access and refresh token for account.
     * Access token will be setted as cookie.
     *
     * @return refreshToken
     */
    @Override
    public String signIn(SignInDTO signInDTO, HttpServletResponse httpServletResponse) {
        AccountDTO accountDTO = userServiceRestClient.requestLogin(signInDTO);
        String accessToken = createAccessToken(accountDTO);

        addAccessTokenCookie(httpServletResponse, accessToken);

        return createRefreshToken(accountDTO);
    }

    /**
     * This method create new access token for account from refresh token.
     * Access token will be setted as cookie.
     */
    @Override
    public void refresh(String refreshToken, HttpServletResponse httpServletResponse) {

        JwsProcessor jwsProcessor = new JwsProcessor(jwtProperties.getPublicKey());
        TokenData tokenData = jwsProcessor.readToken(refreshToken);
        //TODO get data from redis by refresh token
        UUID uuid = UUID.randomUUID();

        AccountDTO accountDTO = userServiceRestClient.getAccountData(uuid);

        String accessToken = createAccessToken(accountDTO);

        addAccessTokenCookie(httpServletResponse, accessToken);
    }


    private String createAccessToken(AccountDTO accountDTO) {
        JwsCreator jwsCreator = new JwsCreator(
                jwtProperties.getPrivateKey(),
                jwtProperties.getAccessTokenExpiration()
        );

        TokenData tokenData = new TokenData(
                accountDTO.getId(), accountDTO.getUuid(), accountDTO.getRoles(), accountDTO.getPermissions()
        );

        return jwsCreator.generateToken(tokenData);
    }

    private String createRefreshToken(AccountDTO accountDTO) {
        //TODO implement

        return createAccessToken(accountDTO);
    }

    private void addAccessTokenCookie(HttpServletResponse response, String accessToken) {
        cookieService.addCookies(
                Collections.singletonMap(AUTHORIZATION_COOKIE, accessToken), response
        );
    }
}

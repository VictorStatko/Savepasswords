package com.statkovit.authorizationservice.services.impl;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkolibraries.jwtprocessing.SignedJweCreator;
import com.statkolibraries.jwtprocessing.SignedJweProcessor;
import com.statkolibraries.jwtprocessing.exception.TokenProcessingException;
import com.statkolibraries.jwtprocessing.payload.TokenData;
import com.statkovit.authorizationservice.feign.UserServiceRestClient;
import com.statkovit.authorizationservice.payloads.AccountDTO;
import com.statkovit.authorizationservice.payloads.SignInDTO;
import com.statkovit.authorizationservice.properties.JweProperties;
import com.statkovit.authorizationservice.services.AuthenticationService;
import com.statkovit.authorizationservice.services.CookieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String AUTHORIZATION_COOKIE = "Authorization";

    private final UserServiceRestClient userServiceRestClient;
    private final JweProperties jweProperties;
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
        SignedJweProcessor jweProcessor = new SignedJweProcessor(jweProperties.getSenderPublicKey(), jweProperties.getRecipientPrivateKey());
        UUID uuid;
        try {
            uuid = jweProcessor.processRefreshJWE(refreshToken);
        } catch (TokenProcessingException e) {
            //TODO replace to real
            throw new LocalizedException(e, HttpStatus.UNAUTHORIZED, "data.data");
        }

        AccountDTO accountDTO = userServiceRestClient.getAccountData(uuid);

        String accessToken = createAccessToken(accountDTO);

        addAccessTokenCookie(httpServletResponse, accessToken);
    }


    private String createAccessToken(AccountDTO accountDTO) {
        SignedJweCreator signedJweCreator = new SignedJweCreator(
                jweProperties.getSenderPrivateKey(),
                jweProperties.getRecipientPublicKey(),
                jweProperties.getAccessTokenExpiration()
        );

        TokenData tokenData = new TokenData(
                accountDTO.getId(), accountDTO.getUuid(), accountDTO.getRoles(), accountDTO.getPermissions()
        );

        return signedJweCreator.generateAccessToken(tokenData);
    }

    private String createRefreshToken(AccountDTO accountDTO) {
        SignedJweCreator signedJweCreator = new SignedJweCreator(
                jweProperties.getSenderPrivateKey(),
                jweProperties.getRecipientPublicKey(),
                jweProperties.getRefreshTokenExpiration()
        );

        return signedJweCreator.generateRefreshToken(accountDTO.getUuid());
    }

    private void addAccessTokenCookie(HttpServletResponse response, String accessToken) {
        cookieService.addCookies(
                Collections.singletonMap(AUTHORIZATION_COOKIE, accessToken), response
        );
    }
}

package com.statkovit.authorizationservice.services;


import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.authorizationservice.utils.AuthenticationFacade;
import com.statkovit.authorizationservice.utils.WebUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
@Service
@RequiredArgsConstructor
public class AuthSessionService {
    private final AuthenticationFacade authenticationFacade;
    private final TokenStore tokenStore;
    private final ConsumerTokenServices consumerTokenServices;

    public void logout() {
        consumerTokenServices.revokeToken(getCurrentToken());
    }

    public List<OAuth2AccessToken> getSessionsList() {
        OAuth2Authentication oauth2Authentication = (OAuth2Authentication) authenticationFacade.getAuthentication();
        OAuth2Request clientAuthentication = oauth2Authentication.getOAuth2Request();
        return new ArrayList<>(tokenStore.findTokensByClientIdAndUserName(clientAuthentication.getClientId(), oauth2Authentication.getName()));
    }

    public void clearAllSessionsExceptCurrent() {
        final String currentToken = getCurrentToken();
        final List<OAuth2AccessToken> tokens = getSessionsList();

        tokens.stream()
                .filter(token -> !currentToken.equals(token.getValue()))
                .forEach(token -> consumerTokenServices.revokeToken(token.getValue()));

    }

    private String getCurrentToken() {
        return WebUtils.getBearerTokenValue().orElseThrow(
                () -> new LocalizedException(
                        "Required request token is not present in the header.",
                        "exceptions.tokenIsNotPresent"
                )
        );
    }
}

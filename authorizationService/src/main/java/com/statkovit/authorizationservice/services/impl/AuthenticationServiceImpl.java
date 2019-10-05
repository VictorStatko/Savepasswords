package com.statkovit.authorizationservice.services.impl;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkolibraries.jwtprocessing.SignedJweCreator;
import com.statkolibraries.jwtprocessing.payload.TokenData;
import com.statkovit.authorizationservice.feign.UserServiceRestClient;
import com.statkovit.authorizationservice.payloads.AccountDTO;
import com.statkovit.authorizationservice.payloads.SignInDTO;
import com.statkovit.authorizationservice.properties.JweProperties;
import com.statkovit.authorizationservice.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserServiceRestClient userServiceRestClient;
    private final JweProperties jweProperties;

    @Override
    public String signIn(SignInDTO signInDTO) {
        AccountDTO accountDTO = userServiceRestClient.requestLogin(signInDTO);

        SignedJweCreator signedJweCreator = new SignedJweCreator(
                jweProperties.getSenderPrivateKey(),
                jweProperties.getRecipientPublicKey(),
                jweProperties.getAccessTokenExpiration()
        );

        TokenData tokenData = new TokenData(
                accountDTO.getId(), accountDTO.getUuid(), accountDTO.getRoles(), accountDTO.getPermissions()
        );

        try {
            return signedJweCreator.generate(tokenData);
        } catch (Exception e) {
            //TODO replace for real key; process exceptions in more detail
            throw new LocalizedException(e.getMessage(), "data");
        }
    }
}

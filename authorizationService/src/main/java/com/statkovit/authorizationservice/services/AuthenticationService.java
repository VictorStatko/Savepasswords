package com.statkovit.authorizationservice.services;

import com.statkovit.authorizationservice.payloads.SignInDTO;
import com.statkovit.authorizationservice.services.transfer.CurrentAccountTokens;

import java.util.Optional;

public interface AuthenticationService {

    CurrentAccountTokens signIn(SignInDTO signInDTO);

    CurrentAccountTokens refresh(String refreshToken);

    Optional<String> exchangeAuthorizationToken(String opaqueAuthToken);
}

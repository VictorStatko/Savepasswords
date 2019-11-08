package com.statkovit.authorizationservice.services;

import com.statkovit.authorizationservice.payloads.SignInDTO;
import com.statkovit.authorizationservice.services.transfer.CurrentAccountTokens;

public interface AuthenticationService {

    CurrentAccountTokens signIn(SignInDTO signInDTO);

    CurrentAccountTokens refresh(String refreshToken);
}

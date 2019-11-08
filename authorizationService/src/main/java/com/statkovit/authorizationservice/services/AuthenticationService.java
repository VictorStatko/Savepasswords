package com.statkovit.authorizationservice.services;

import com.statkovit.authorizationservice.payloads.SignInDTO;
import com.statkovit.authorizationservice.services.transfer.TokensCreationResult;

public interface AuthenticationService {

    TokensCreationResult signIn(SignInDTO signInDTO);
}

package com.statkovit.authorizationservice.services;

import com.statkovit.authorizationservice.payloads.SignInDTO;

public interface AuthenticationService {

    String signIn(SignInDTO signInDTO);
}

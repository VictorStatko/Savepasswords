package com.statkovit.authorizationservice.services;

import com.statkovit.authorizationservice.payloads.SignInDTO;

import javax.servlet.http.HttpServletResponse;

public interface AuthenticationService {

    String signIn(SignInDTO signInDTO, HttpServletResponse httpServletResponse);

    void refresh(String refreshToken, HttpServletResponse httpServletResponse);
}

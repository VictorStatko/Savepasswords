package com.statkovit.authorizationservice.rest;

import com.statkovit.authorizationservice.payloads.SignInDTO;

import javax.servlet.http.HttpServletResponse;

public interface AuthenticationRestService {

    void signIn(SignInDTO signInDTO, HttpServletResponse httpServletResponse);

    void refresh(String refreshToken, HttpServletResponse httpServletResponse);

    void exchange(String opaqueAccessToken, HttpServletResponse httpServletResponse);
}

package com.statkovit.authorizationservice.rest;

import com.statkovit.authorizationservice.payloads.SignInDTO;

import javax.servlet.http.HttpServletResponse;

public interface AuthenticationRestService {

    void signIn(SignInDTO signInDTO, HttpServletResponse httpServletResponse);

}

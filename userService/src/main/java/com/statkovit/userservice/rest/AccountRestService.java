package com.statkovit.userservice.rest;

import com.statkovit.userservice.dto.AccountLoginDTO;
import com.statkovit.userservice.dto.CredentialsDTO;

public interface AccountRestService {

    void signUp(CredentialsDTO credentialsDTO);

    AccountLoginDTO requestLogin(CredentialsDTO requestLoginDTO);

}

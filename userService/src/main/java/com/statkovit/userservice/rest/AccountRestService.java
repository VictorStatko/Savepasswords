package com.statkovit.userservice.rest;

import com.statkovit.userservice.dto.*;

import java.util.UUID;

public interface AccountRestService {

    void signUp(SignUpDTO signUpDTO);

    AccountDataDTO requestLogin(CredentialsDTO requestLoginDTO);

    AccountDataDTO getAccountData(UUID accountUuid);

    BooleanDTO isAccountExists(AccountExistsDTO accountExistsDTO);
}

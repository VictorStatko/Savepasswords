package com.statkovit.userservice.rest;

import com.statkovit.userservice.dto.AccountDataDTO;
import com.statkovit.userservice.dto.AccountExistsDTO;
import com.statkovit.userservice.dto.BooleanDTO;
import com.statkovit.userservice.dto.CredentialsDTO;

import java.util.UUID;

public interface AccountRestService {

    void signUp(CredentialsDTO credentialsDTO);

    AccountDataDTO requestLogin(CredentialsDTO requestLoginDTO);

    AccountDataDTO getAccountData(UUID accountUuid);

    BooleanDTO isAccountExists(AccountExistsDTO accountExistsDTO);
}

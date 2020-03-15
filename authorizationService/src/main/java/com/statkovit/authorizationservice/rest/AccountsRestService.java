package com.statkovit.authorizationservice.rest;

import com.statkovit.authorizationservice.payload.AccountDto;
import com.statkovit.authorizationservice.payload.ExtendedAccountDto;
import com.statkovit.authorizationservice.payload.StringDto;

public interface AccountsRestService {

    AccountDto create(AccountDto accountDto);

    ExtendedAccountDto getCurrentAccountDataFromAuth();

    StringDto getClientEncryptionSalt(String email);
}

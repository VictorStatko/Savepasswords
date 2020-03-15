package com.statkovit.authorizationservice.rest;

import com.statkovit.authorizationservice.payload.AccountDto;
import com.statkovit.authorizationservice.payload.ExtendedAccountDto;
import com.statkovit.authorizationservice.payload.KeyPairDto;
import com.statkovit.authorizationservice.payload.StringDto;

public interface AccountsRestService {

    AccountDto create(AccountDto accountDto);

    ExtendedAccountDto getCurrentAccountData();

    KeyPairDto getCurrentAccountKeypair();

    StringDto getClientEncryptionSalt(String email);
}

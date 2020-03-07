package com.statkovit.authorizationservice.rest;

import com.statkovit.authorizationservice.payload.AccountDto;
import com.statkovit.authorizationservice.payload.ExtendedAccountDto;

public interface AccountsRestService {

    AccountDto create(AccountDto accountDto);

    ExtendedAccountDto getCurrentAccountDataFromAuth();
}

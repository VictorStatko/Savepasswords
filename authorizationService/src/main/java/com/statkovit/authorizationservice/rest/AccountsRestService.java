package com.statkovit.authorizationservice.rest;

import com.statkovit.authorizationservice.payload.AccountDto;

public interface AccountsRestService {

    AccountDto create(AccountDto accountDto);

    AccountDto getCurrentAccountDataFromAuth();
}

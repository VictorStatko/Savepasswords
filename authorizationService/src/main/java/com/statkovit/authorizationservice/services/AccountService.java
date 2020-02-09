package com.statkovit.authorizationservice.services;

import com.statkovit.authorizationservice.domain.Account;
import com.statkovit.authorizationservice.payload.AccountDto;

public interface AccountService {
    Account create(AccountDto account);

    Account getByEmail(String email);
}

package com.statkovit.authorizationservice.services;

import com.statkovit.authorizationservice.domain.Account;
import com.statkovit.authorizationservice.payload.AccountDto;
import org.apache.commons.lang3.tuple.Pair;

public interface AccountService {
    Account create(AccountDto account);

    Account getByEmail(String email);

    Pair<String, String> getAccountKeypair(String email);
}

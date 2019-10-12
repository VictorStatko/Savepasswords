package com.statkovit.userservice.services;

import com.statkovit.userservice.domain.Account;

import java.util.UUID;

public interface AccountService {

    void signUp(Account newAccount);

    Account getByEmail(String email);

    Account requestLogin(String email, String password);

    Account getByUuid(UUID uuid);
}

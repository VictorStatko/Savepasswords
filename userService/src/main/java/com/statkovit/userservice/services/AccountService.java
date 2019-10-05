package com.statkovit.userservice.services;

import com.statkovit.userservice.domain.Account;

public interface AccountService {

    void signUp(Account newAccount);

    Account getByEmail(String email);

    Account requestLogin(String email, String password);
}

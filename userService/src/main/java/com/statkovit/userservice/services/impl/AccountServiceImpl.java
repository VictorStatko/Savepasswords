package com.statkovit.userservice.services.impl;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.userservice.constants.ServerConstants;
import com.statkovit.userservice.domain.Account;
import com.statkovit.userservice.repository.AccountRepository;
import com.statkovit.userservice.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public void signUp(Account newAccount) {
        if (accountRepository.existsByEmail(newAccount.getEmail())) {
            throw new LocalizedException(
                    String.format("User with email '%s' already exists", newAccount.getEmail()),
                    ServerConstants.ERROR_KEY
            );
        }
        //TODO encode password
        accountRepository.save(newAccount);
    }

    @Override
    public Account getByEmail(String email) {
        return accountRepository.getByEmail(email).orElseThrow(() ->
                new LocalizedException(
                        new EntityNotFoundException("Account with email = " + email + " has not been found."),
                        //TODO replace for real key
                        "data"
                )
        );
    }

    @Override
    public Account getByUuid(UUID uuid) {
        return accountRepository.getByUuid(uuid).orElseThrow(() ->
                new LocalizedException(
                        new EntityNotFoundException("Account with uuid = " + uuid + " has not been found."),
                        //TODO replace for real key
                        "data"
                )
        );
    }

    @Override
    public Account requestLogin(String email, String password) {
        Account account = getByEmail(email);
        //TODO encrypted password
        if (!Objects.equals(account.getPassword(), password)) {
            throw new LocalizedException(
                    "Password for account with email " + email + " is invalid.",
                    //TODO replace for real key
                    "data"
            );
        }
        return account;
    }
}

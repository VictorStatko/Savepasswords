package com.statkovit.userservice.services.impl;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.userservice.constants.ServerConstants;
import com.statkovit.userservice.domain.Account;
import com.statkovit.userservice.repository.AccountRepository;
import com.statkovit.userservice.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
}

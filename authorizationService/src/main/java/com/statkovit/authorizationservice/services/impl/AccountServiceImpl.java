package com.statkovit.authorizationservice.services.impl;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.authorizationservice.domain.Account;
import com.statkovit.authorizationservice.payload.AccountDto;
import com.statkovit.authorizationservice.repositories.AccountRepository;
import com.statkovit.authorizationservice.services.AccountService;
import com.statkovit.authorizationservice.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RoleService roleService;

    @Override
    @Transactional
    public Account create(AccountDto accountDto) {
        if (accountRepository.existsByEmail(accountDto.getEmail())) {
            throw new LocalizedException(
                    String.format("Account with email %s already exists.", accountDto.getEmail()),
                    "exceptions.emailAlreadyExists"
            );
        }

        final String passwordHash = passwordEncoder.encode(accountDto.getPassword());

        final Account account = new Account();

        account.setPassword(passwordHash);
        account.setEmail(accountDto.getEmail());
        account.setRole(roleService.getAccountOwnerRole());

        return accountRepository.save(account);
    }

    //TODO replace exceptions.error
    @Override
    @Transactional(readOnly = true)
    public Account getByEmail(String email) {
        return accountRepository.getByEmail(email).orElseThrow(
                () -> new LocalizedException(
                        new EntityNotFoundException("Account with email = " + email + " has not been found."),
                        "exceptions.error"
                )
        );
    }
}

package com.statkovit.authorizationservice.rest.impl;

import com.statkovit.authorizationservice.domain.Account;
import com.statkovit.authorizationservice.mappers.AccountMapper;
import com.statkovit.authorizationservice.payload.AccountDto;
import com.statkovit.authorizationservice.rest.AccountsRestService;
import com.statkovit.authorizationservice.services.AccountService;
import com.statkovit.authorizationservice.utils.AuthenticationFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountsRestServiceImpl implements AccountsRestService {
    private final AccountMapper accountMapper;
    private final AccountService accountService;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public AccountDto create(AccountDto accountDto) {
        Account account = accountService.create(accountDto);
        return accountMapper.toDto(account);
    }

    @Override
    public AccountDto getCurrentAccountDataFromAuth() {
        Account account = accountService.getByEmail(
                authenticationFacade.getAuthentication().getName()
        );
        return accountMapper.toDto(account);
    }
}

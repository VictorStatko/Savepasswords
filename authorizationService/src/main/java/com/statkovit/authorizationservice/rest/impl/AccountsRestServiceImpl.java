package com.statkovit.authorizationservice.rest.impl;

import com.statkovit.authorizationservice.domain.Account;
import com.statkovit.authorizationservice.mappers.AccountMapper;
import com.statkovit.authorizationservice.payload.AccountDto;
import com.statkovit.authorizationservice.rest.AccountsRestService;
import com.statkovit.authorizationservice.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountsRestServiceImpl implements AccountsRestService {
    private final AccountMapper accountMapper;
    private final AccountService accountService;

    @Override
    public AccountDto create(AccountDto accountDto) {
        Account account = accountService.create(accountDto);
        return accountMapper.toDto(account);
    }
}

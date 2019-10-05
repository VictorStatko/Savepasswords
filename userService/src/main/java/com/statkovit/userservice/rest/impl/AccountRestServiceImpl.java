package com.statkovit.userservice.rest.impl;

import com.statkovit.userservice.domain.Account;
import com.statkovit.userservice.dto.AccountLoginDTO;
import com.statkovit.userservice.dto.CredentialsDTO;
import com.statkovit.userservice.mappers.AccountMapper;
import com.statkovit.userservice.rest.AccountRestService;
import com.statkovit.userservice.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountRestServiceImpl implements AccountRestService {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @Override
    public void signUp(CredentialsDTO credentialsDTO) {
        Account account = accountMapper.toEntity(credentialsDTO);
        accountService.signUp(account);
    }

    @Override
    public AccountLoginDTO requestLogin(CredentialsDTO requestLoginDTO) {
        Account account = accountService.requestLogin(
                requestLoginDTO.getEmail(), requestLoginDTO.getPassword()
        );

        return accountMapper.toAccountLoginDto(account);
    }
}

package com.statkovit.userservice.rest.impl;

import com.statkovit.userservice.domain.Account;
import com.statkovit.userservice.dto.*;
import com.statkovit.userservice.mappers.AccountMapper;
import com.statkovit.userservice.rest.AccountRestService;
import com.statkovit.userservice.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountRestServiceImpl implements AccountRestService {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @Override
    public void signUp(SignUpDTO signUpDTO) {
        Account account = accountMapper.toEntity(signUpDTO);
        accountService.signUp(account);
    }

    @Override
    public AccountDataDTO requestLogin(CredentialsDTO requestLoginDTO) {
        Account account = accountService.requestLogin(
                requestLoginDTO.getEmail(), requestLoginDTO.getPassword()
        );

        return accountMapper.toAccountDataDto(account);
    }

    @Override
    public AccountDataDTO getAccountData(UUID accountUuid) {
        Account account = accountService.getByUuid(accountUuid);

        return accountMapper.toAccountDataDto(account);
    }

    @Override
    public BooleanDTO isAccountExists(AccountExistsDTO accountExistsDTO) {
        boolean accountExists = accountService.existsByEmail(accountExistsDTO.getEmail());
        return new BooleanDTO(accountExists);
    }
}

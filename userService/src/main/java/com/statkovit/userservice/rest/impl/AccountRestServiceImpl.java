package com.statkovit.userservice.rest.impl;

import com.statkovit.userservice.domain.Account;
import com.statkovit.userservice.dto.SignUpDTO;
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
    public void signUp(SignUpDTO signUpDTO) {
        Account account = accountMapper.toEntity(signUpDTO);
        accountService.signUp(account);
    }
}

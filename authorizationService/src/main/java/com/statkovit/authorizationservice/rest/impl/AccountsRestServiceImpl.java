package com.statkovit.authorizationservice.rest.impl;

import com.statkovit.authorizationservice.domain.Account;
import com.statkovit.authorizationservice.mappers.AccountMapper;
import com.statkovit.authorizationservice.payload.AccountDto;
import com.statkovit.authorizationservice.payload.ExtendedAccountDto;
import com.statkovit.authorizationservice.payload.KeyPairDto;
import com.statkovit.authorizationservice.payload.StringDto;
import com.statkovit.authorizationservice.rest.AccountsRestService;
import com.statkovit.authorizationservice.services.AccountService;
import com.statkovit.authorizationservice.utils.AuthenticationFacade;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
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
    public ExtendedAccountDto getCurrentAccountData() {
        Account account = accountService.getByEmail(
                authenticationFacade.getAuthentication().getName()
        );
        return accountMapper.toExtendedDto(account);
    }

    @Override
    public KeyPairDto getCurrentAccountKeypair() {
        Pair<String, String> keypair = accountService.getAccountKeypair(
                authenticationFacade.getAuthentication().getName()
        );

        return new KeyPairDto(keypair.getLeft(), keypair.getRight());
    }

    @Override
    public StringDto getClientEncryptionSalt(String email) {
        Account account = accountService.getByEmail(email);
        return new StringDto(account.getClientPasswordSalt());
    }
}

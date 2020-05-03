package com.statkovit.authorizationservice.domainServices;

import com.statkovit.authorizationservice.entities.Account;
import com.statkovit.authorizationservice.mappers.AccountMapper;
import com.statkovit.authorizationservice.payload.AccountDto;
import com.statkovit.authorizationservice.payload.ExtendedAccountDto;
import com.statkovit.authorizationservice.payload.KeyPairDto;
import com.statkovit.authorizationservice.payload.StringDto;
import com.statkovit.authorizationservice.services.AccountService;
import com.statkovit.authorizationservice.utils.AuthenticationFacade;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AccountsDomainService {
    private final AccountMapper accountMapper;
    private final AccountService accountService;
    private final AuthenticationFacade authenticationFacade;

    @Transactional
    public AccountDto create(AccountDto accountDto) {
        Account account = accountService.create(accountDto);
        return accountMapper.toDto(account);
    }

    public ExtendedAccountDto getCurrentExtendedAccountData() {
        Account account = accountService.getByEmail(
                authenticationFacade.getAuthentication().getName()
        );
        return accountMapper.toExtendedDto(account);
    }

    public AccountDto getCurrentAccountData() {
        Account account = accountService.getByEmail(
                authenticationFacade.getAuthentication().getName()
        );
        return accountMapper.toDto(account);
    }

    public KeyPairDto getCurrentAccountKeypair() {
        Pair<String, String> keypair = accountService.getAccountKeypair(
                authenticationFacade.getAuthentication().getName()
        );

        return new KeyPairDto(keypair.getLeft(), keypair.getRight());
    }

    public StringDto getClientEncryptionSalt(String email) {
        Account account = accountService.getByEmail(email);
        return new StringDto(account.getClientPasswordSalt());
    }
}

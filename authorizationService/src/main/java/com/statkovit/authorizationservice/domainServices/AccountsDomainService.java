package com.statkovit.authorizationservice.domainServices;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
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

    @Transactional
    public AccountDto confirmRegistration(String verificationCode) {
        Account account = accountService.confirmRegistration(verificationCode);
        return accountMapper.toDto(account);
    }

    @Transactional
    public void resendVerificationCode(String email) {
        Account account = accountService.getByEmail(email);

        if (account.isEnabled()) {
            throw new LocalizedException(
                    String.format("Account with email %s is already verified.", email),
                    "exceptions.accountAlreadyVerified"
            );
        }

        accountService.resendVerificationCode(account);
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

    @Transactional
    public void removeCurrentAccount() {
        Account account = accountService.getByEmail(
                authenticationFacade.getAuthentication().getName()
        );

        accountService.remove(account);
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

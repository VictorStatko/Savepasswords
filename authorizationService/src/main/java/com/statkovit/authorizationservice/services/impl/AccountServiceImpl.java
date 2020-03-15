package com.statkovit.authorizationservice.services.impl;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkolibraries.utils.SecuredRandomStringGenerator;
import com.statkovit.authorizationservice.domain.Account;
import com.statkovit.authorizationservice.payload.AccountDto;
import com.statkovit.authorizationservice.properties.CustomProperties;
import com.statkovit.authorizationservice.repositories.AccountRepository;
import com.statkovit.authorizationservice.services.AccountService;
import com.statkovit.authorizationservice.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RoleService roleService;
    private final CustomProperties customProperties;

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
        account.setClientPasswordSalt(accountDto.getClientPasswordSalt());
        account.setEmail(accountDto.getEmail());
        account.setRole(roleService.getAccountOwnerRole());
        account.setPublicKey(accountDto.getPublicKey());

        String privateKeySalt = new String(
                Hex.encode(
                        new SecuredRandomStringGenerator(16).generate().getBytes()
                )
        );

        TextEncryptor privateKeyEncryptor = Encryptors.text(
                new String(Base64.getDecoder().decode(customProperties.getAes().getKey())),
                privateKeySalt
        );

        account.setPrivateKey(privateKeyEncryptor.encrypt(accountDto.getPrivateKey()));
        account.setPrivateKeySalt(privateKeySalt);

        return accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Account getByEmail(String email) {
        return accountRepository.getByEmail(email).orElseThrow(
                () -> new LocalizedException(
                        new EntityNotFoundException("Account with email = " + email + " has not been found."),
                        "exceptions.accountNotFoundByEmail"
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Pair<String, String> getAccountKeypair(String email) {
        final Account account = getByEmail(email);

        TextEncryptor privateKeyEncryptor = Encryptors.text(
                new String(Base64.getDecoder().decode(customProperties.getAes().getKey())),
                account.getPrivateKeySalt()
        );

        final String decryptedPrivateKey = privateKeyEncryptor.decrypt(account.getPrivateKey());

        return ImmutablePair.of(decryptedPrivateKey, account.getPublicKey());
    }
}

package com.statkovit.authorizationservice.services;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkolibraries.utils.SecuredRandomStringGenerator;
import com.statkovit.authorizationservice.entities.Account;
import com.statkovit.authorizationservice.events.AccountCreatedEvent;
import com.statkovit.authorizationservice.mappers.AccountKafkaMapper;
import com.statkovit.authorizationservice.payload.AccountDto;
import com.statkovit.authorizationservice.properties.CustomProperties;
import com.statkovit.authorizationservice.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.ApplicationEventPublisher;
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
@Transactional(readOnly = true)
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RoleService roleService;
    private final CustomProperties customProperties;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AccountKafkaMapper accountKafkaMapper;

    @Transactional
    public Account create(AccountDto accountDto) {
        if (accountRepository.existsByEmail(accountDto.getEmail())) {
            throw new LocalizedException(
                    String.format("Account with email %s already exists.", accountDto.getEmail()),
                    "exceptions.emailAlreadyExists"
            );
        }

        final String passwordHash = passwordEncoder.encode(accountDto.getPassword());

        Account account = new Account();

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

        account = accountRepository.save(account);

        applicationEventPublisher.publishEvent(
                new AccountCreatedEvent(accountKafkaMapper.toDto(account))
        );

        return account;
    }

    public Account getByEmail(String email) {
        return accountRepository.getByEmail(email).orElseThrow(
                () -> new LocalizedException(
                        new EntityNotFoundException("Account with email = " + email + " has not been found."),
                        "exceptions.accountNotFoundByEmail"
                )
        );
    }

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

package com.statkovit.authorizationservice.services;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkolibraries.utils.SecuredRandomStringGenerator;
import com.statkovit.authorizationservice.constants.ServerConstants;
import com.statkovit.authorizationservice.entities.Account;
import com.statkovit.authorizationservice.events.AccountRemovedEvent;
import com.statkovit.authorizationservice.events.AccountVerificationRequestedEvent;
import com.statkovit.authorizationservice.events.AccountVerifiedEvent;
import com.statkovit.authorizationservice.mappers.AccountKafkaMapper;
import com.statkovit.authorizationservice.payload.AccountDto;
import com.statkovit.authorizationservice.properties.CustomProperties;
import com.statkovit.authorizationservice.repositories.AccountRepository;
import com.statkovit.authorizationservice.services.RegistrationConfirmationVerificationService.VerificationCode;
import com.statkovit.authorizationservice.utils.WebUtils;
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
    private final AuthSessionService authSessionService;
    private final RegistrationConfirmationVerificationService registrationConfirmationVerificationService;

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
        account.setEnabled(false);

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

        VerificationCode verificationCode = registrationConfirmationVerificationService.createNewVerificationCode(account.getId());

        String locale = WebUtils.getLocaleHeader().orElse(ServerConstants.DEFAULT_LOCALE);

        applicationEventPublisher.publishEvent(
                new AccountVerificationRequestedEvent(
                        locale, account.getEmail(), account.getUuid(),
                        verificationCode.getVerificationCode(), verificationCode.getExpirationInHours()
                )
        );

        return account;
    }

    @Transactional
    public Account confirmRegistration(String verificationCode) {
        Long accountId = registrationConfirmationVerificationService.confirmRegistration(verificationCode);
        Account account = getById(accountId);
        account.setEnabled(true);
        account = accountRepository.save(account);

        applicationEventPublisher.publishEvent(
                new AccountVerifiedEvent(accountKafkaMapper.toDto(account))
        );

        return account;
    }


    @Transactional
    public void resendVerificationCode(Account account) {
        String locale = WebUtils.getLocaleHeader().orElse(ServerConstants.DEFAULT_LOCALE);

        VerificationCode verificationCode = registrationConfirmationVerificationService.createNewVerificationCode(account.getId());

        applicationEventPublisher.publishEvent(
                new AccountVerificationRequestedEvent(
                        locale, account.getEmail(), account.getUuid(),
                        verificationCode.getVerificationCode(), verificationCode.getExpirationInHours()
                )
        );
    }

    @Transactional
    public void remove(Account account) {
        accountRepository.delete(account);

        applicationEventPublisher.publishEvent(
                new AccountRemovedEvent(accountKafkaMapper.toDto(account))
        );

        authSessionService.clearAllSessions();
    }

    public Account getByEmail(String email) {
        return accountRepository.getByEmail(email).orElseThrow(
                () -> new LocalizedException(
                        new EntityNotFoundException("Account with email = " + email + " has not been found."),
                        "exceptions.accountNotFoundByEmail"
                )
        );
    }

    public Account getById(Long id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new LocalizedException(
                        new EntityNotFoundException("Account with id = " + id + " has not been found."),
                        "exceptions.accountNotFoundById"
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

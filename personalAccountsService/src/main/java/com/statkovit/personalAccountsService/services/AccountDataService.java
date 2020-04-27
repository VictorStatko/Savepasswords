package com.statkovit.personalAccountsService.services;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.domain.AccountData;
import com.statkovit.personalAccountsService.repository.AccountDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountDataService {

    private final AccountDataRepository accountDataRepository;

    @Transactional
    public AccountData save(AccountData accountData) {
        return accountDataRepository.save(accountData);
    }

    public AccountData internalGetById(Long id) {
        return accountDataRepository.findById(id).orElseThrow(
                () -> new LocalizedException(
                        new EntityNotFoundException("Account data with id = " + id + " has not been found."),
                        "exceptions.accountDataNotFoundById"
                )
        );
    }

    public AccountData internalGetByUuid(UUID uuid) {
        return accountDataRepository.findByUuid(uuid).orElseThrow(
                () -> new LocalizedException(
                        new EntityNotFoundException("Account data with uuid = " + uuid + " has not been found."),
                        "exceptions.accountDataNotFoundByUuid"
                )
        );
    }

    public AccountData internalGetByEmail(String email) {
        return accountDataRepository.findByEmail(email).orElseThrow(
                () -> new LocalizedException(
                        new EntityNotFoundException("Account data with email = " + email + " has not been found."),
                        "exceptions.accountDataNotFoundByEmail"
                )
        );
    }
}

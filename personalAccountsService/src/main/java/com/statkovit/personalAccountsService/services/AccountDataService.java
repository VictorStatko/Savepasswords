package com.statkovit.personalAccountsService.services;

import com.statkovit.personalAccountsService.domain.AccountData;
import com.statkovit.personalAccountsService.repository.AccountDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountDataService {

    private final AccountDataRepository accountDataRepository;

    @Transactional
    public AccountData save(AccountData accountData) {
        return accountDataRepository.save(accountData);
    }
}

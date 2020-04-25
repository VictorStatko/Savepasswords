package com.statkovit.personalAccountsService.services.impl;

import com.statkovit.personalAccountsService.domain.duplication.AccountData;
import com.statkovit.personalAccountsService.repository.DuplicatedAccountDataRepository;
import com.statkovit.personalAccountsService.services.AccountDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountDataServiceImpl implements AccountDataService {

    private final DuplicatedAccountDataRepository duplicatedAccountDataRepository;

    @Transactional
    @Override
    public AccountData save(AccountData accountData) {
        return duplicatedAccountDataRepository.save(accountData);
    }
}

package com.statkovit.personalAccountsService.dataService.impl;

import com.statkovit.personalAccountsService.dataService.AccountDataDataService;
import com.statkovit.personalAccountsService.domain.duplication.AccountData;
import com.statkovit.personalAccountsService.payload.AccountDataDto;
import com.statkovit.personalAccountsService.payload.converters.AccountDataConverter;
import com.statkovit.personalAccountsService.services.AccountDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountDataDataServiceImpl implements AccountDataDataService {
    private final AccountDataConverter dataConverter;
    private final AccountDataService accountDataService;

    @Override
    public void create(AccountDataDto dto) {
        AccountData data = new AccountData();

        dataConverter.toEntity(dto, data);

        accountDataService.save(data);
    }
}

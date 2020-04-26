package com.statkovit.personalAccountsService.domainService;

import com.statkovit.personalAccountsService.domain.AccountData;
import com.statkovit.personalAccountsService.payload.AccountDataDto;
import com.statkovit.personalAccountsService.payload.converters.AccountDataConverter;
import com.statkovit.personalAccountsService.services.AccountDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountDataDomainService {
    private final AccountDataConverter dataConverter;
    private final AccountDataService accountDataService;

    public void create(AccountDataDto dto) {
        AccountData data = new AccountData();

        dataConverter.toEntity(dto, data);

        accountDataService.save(data);
    }
}

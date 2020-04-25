package com.statkovit.personalAccountsService.payload.converters;

import com.statkovit.personalAccountsService.domain.duplication.AccountData;
import com.statkovit.personalAccountsService.payload.AccountDataDto;
import com.statkovit.personalAccountsService.payload.mappers.AccountDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountDataConverter {

    private final AccountDataMapper accountDataMapper;

    public void toEntity(AccountDataDto dto, AccountData data) {
        accountDataMapper.toEntity(dto, data);
    }

    public AccountDataDto toDto(AccountData data) {
        return accountDataMapper.toDto(data);
    }
}

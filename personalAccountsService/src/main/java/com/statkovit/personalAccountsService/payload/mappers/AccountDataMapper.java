package com.statkovit.personalAccountsService.payload.mappers;

import com.statkovit.personalAccountsService.domain.duplication.AccountData;
import com.statkovit.personalAccountsService.payload.AccountDataDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountDataMapper {

    public void toEntity(AccountDataDto dto, AccountData accountData) {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.map(dto, accountData);
    }

    public AccountDataDto toDto(AccountData accountData) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(accountData, AccountDataDto.class);
    }

}

package com.statkovit.personalAccountsService.mappers;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonalAccountMapper {

    public PersonalAccount toEntity(PersonalAccountDto personalAccountDto) {
        ModelMapper modelMapper = new ModelMapper();

        PersonalAccount personalAccount = modelMapper.map(personalAccountDto, PersonalAccount.class);
        personalAccount.setAccountEntityId(SecurityUtils.getCurrentAccountEntityId());

        return personalAccount;
    }

    public PersonalAccountDto toDto(PersonalAccount personalAccount) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(personalAccount, PersonalAccountDto.class);
    }
}

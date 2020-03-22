package com.statkovit.personalAccountsService.mappers;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.encryptors.PersonalAccountsEncryptor;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonalAccountMapper {
    private final PersonalAccountsEncryptor personalAccountsEncryptor;

    public PersonalAccount toEntity(PersonalAccountDto personalAccountDto) {
        ModelMapper modelMapper = new ModelMapper();

        PersonalAccount personalAccount = modelMapper.map(personalAccountDto, PersonalAccount.class);

        personalAccountsEncryptor.encryptFields(personalAccount);

        return personalAccount;
    }

    public PersonalAccountDto toDto(PersonalAccount personalAccount) {
        ModelMapper modelMapper = new ModelMapper();

        PersonalAccountDto dto = modelMapper.map(personalAccount, PersonalAccountDto.class);

        personalAccountsEncryptor.decryptFields(personalAccount.getFieldsEncryptionSalt(), dto);

        return dto;
    }
}

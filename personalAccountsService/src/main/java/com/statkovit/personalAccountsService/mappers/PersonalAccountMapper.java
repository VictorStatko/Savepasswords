package com.statkovit.personalAccountsService.mappers;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.encryptors.PersonalAccountsEncryptor;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PersonalAccountMapper {
    private final PersonalAccountsEncryptor personalAccountsEncryptor;

    public PersonalAccount toEntity(PersonalAccountDto personalAccountDto, PersonalAccount personalAccount) {
        final boolean newMode = Objects.isNull(personalAccount.getUuid());

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<PersonalAccountDto, PersonalAccount>() {
            @Override
            protected void configure() {
                skip(destination.getUuid());
            }
        });

        modelMapper.map(personalAccountDto, personalAccount);

        personalAccountsEncryptor.encryptFields(personalAccount);

        if (newMode) {
            personalAccount.setAccountEntityId(SecurityUtils.getCurrentAccountEntityId());
        }

        return personalAccount;
    }

    public PersonalAccountDto toDto(PersonalAccount personalAccount) {
        ModelMapper modelMapper = new ModelMapper();

        PersonalAccountDto dto = modelMapper.map(personalAccount, PersonalAccountDto.class);

        personalAccountsEncryptor.decryptFields(personalAccount.getFieldsEncryptionSalt(), dto);

        return dto;
    }
}

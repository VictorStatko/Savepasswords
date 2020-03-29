package com.statkovit.personalAccountsService.payload.converters;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.encryptors.PersonalAccountsEncryptor;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.payload.mappers.PersonalAccountMapper;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PersonalAccountConverter {

    private final PersonalAccountMapper personalAccountMapper;
    private final PersonalAccountsEncryptor personalAccountsEncryptor;
    private final SecurityUtils securityUtils;

    public void toEntity(PersonalAccountDto personalAccountDto, PersonalAccount personalAccount) {
        final boolean newMode = Objects.isNull(personalAccount.getUuid());

        personalAccountMapper.toEntity(personalAccountDto, personalAccount);

        personalAccountsEncryptor.encryptFields(personalAccount);

        if (newMode) {
            personalAccount.setAccountEntityId(securityUtils.getCurrentAccountEntityId());
        }
    }

    public PersonalAccountDto toDto(PersonalAccount personalAccount) {
        PersonalAccountDto dto = personalAccountMapper.toDto(personalAccount);

        personalAccountsEncryptor.decryptFields(personalAccount.getFieldsEncryptionSalt(), dto);

        return dto;
    }
}

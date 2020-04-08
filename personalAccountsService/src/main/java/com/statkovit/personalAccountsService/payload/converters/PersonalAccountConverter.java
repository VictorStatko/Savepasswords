package com.statkovit.personalAccountsService.payload.converters;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.encryptors.PersonalAccountsEncryptor;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.payload.mappers.PersonalAccountMapper;
import com.statkovit.personalAccountsService.services.PersonalAccountFolderService;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public final class PersonalAccountConverter {

    private final PersonalAccountMapper personalAccountMapper;
    private final PersonalAccountsEncryptor personalAccountsEncryptor;
    private final SecurityUtils securityUtils;
    private final PersonalAccountFolderService folderService;

    public void toEntity(PersonalAccountDto personalAccountDto, PersonalAccount personalAccount) {
        final boolean newMode = Objects.isNull(personalAccount.getUuid());

        personalAccountMapper.toEntity(personalAccountDto, personalAccount);

        personalAccountsEncryptor.encryptFields(personalAccount);

        if (newMode) {
            personalAccount.setAccountEntityId(securityUtils.getCurrentAccountEntityId());
        }

        if (Objects.isNull(personalAccountDto.getFolderUuid())) {
            personalAccount.setFolder(null);
        } else {
            personalAccount.setFolder(folderService.getByUuid(personalAccountDto.getFolderUuid()));
        }
    }

    public PersonalAccountDto toDto(PersonalAccount personalAccount) {
        PersonalAccountDto dto = personalAccountMapper.toDto(personalAccount);

        personalAccountsEncryptor.decryptFields(personalAccount.getFieldsEncryptionSalt(), dto);

        return dto;
    }
}

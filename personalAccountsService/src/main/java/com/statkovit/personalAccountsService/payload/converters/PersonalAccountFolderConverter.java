package com.statkovit.personalAccountsService.payload.converters;

import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.payload.PersonalAccountFolderDto;
import com.statkovit.personalAccountsService.payload.mappers.PersonalAccountFolderMapper;
import com.statkovit.personalAccountsService.services.AccountDataService;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public final class PersonalAccountFolderConverter {

    private final SecurityUtils securityUtils;
    private final PersonalAccountFolderMapper folderMapper;
    private final AccountDataService accountDataService;

    public void toEntity(PersonalAccountFolderDto folderDto, PersonalAccountFolder folder) {
        final boolean newMode = Objects.isNull(folder.getUuid());

        folderMapper.toEntity(folderDto, folder);

        if (newMode) {
            folder.setDuplicatedAccountEntity(accountDataService.internalGetById(securityUtils.getCurrentAccountEntityId()));
            folder.setAccountEntityId(folder.getDuplicatedAccountEntity().getId());
        }
    }

    public PersonalAccountFolderDto toDto(PersonalAccountFolder personalAccountFolder) {
        return folderMapper.toDto(personalAccountFolder);
    }
}

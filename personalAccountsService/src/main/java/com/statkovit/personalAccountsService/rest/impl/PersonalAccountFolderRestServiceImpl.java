package com.statkovit.personalAccountsService.rest.impl;

import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.payload.PersonalAccountFolderDto;
import com.statkovit.personalAccountsService.payload.converters.PersonalAccountFolderConverter;
import com.statkovit.personalAccountsService.rest.PersonalAccountFolderRestService;
import com.statkovit.personalAccountsService.services.PersonalAccountFolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalAccountFolderRestServiceImpl implements PersonalAccountFolderRestService {

    private final PersonalAccountFolderConverter folderConverter;
    private final PersonalAccountFolderService personalAccountFolderService;

    @Override
    public PersonalAccountFolderDto create(PersonalAccountFolderDto dto) {
        PersonalAccountFolder folderForSave = new PersonalAccountFolder();

        folderConverter.toEntity(dto, folderForSave);

        folderForSave = personalAccountFolderService.save(folderForSave);

        return folderConverter.toDto(folderForSave);
    }
}

package com.statkovit.personalAccountsService.rest;

import com.statkovit.personalAccountsService.payload.PersonalAccountFolderDto;

import java.util.List;
import java.util.UUID;

public interface PersonalAccountFolderRestService {

    PersonalAccountFolderDto create(PersonalAccountFolderDto dto);

    PersonalAccountFolderDto update(UUID folderUuid, PersonalAccountFolderDto dto);

    List<PersonalAccountFolderDto> getListOfCurrentAccountEntity();
}

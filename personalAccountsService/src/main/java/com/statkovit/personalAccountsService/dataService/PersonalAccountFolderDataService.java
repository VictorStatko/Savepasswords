package com.statkovit.personalAccountsService.dataService;

import com.statkovit.personalAccountsService.enums.FolderRemovalOptions;
import com.statkovit.personalAccountsService.payload.PersonalAccountFolderDto;

import java.util.List;
import java.util.UUID;

public interface PersonalAccountFolderDataService {

    PersonalAccountFolderDto create(PersonalAccountFolderDto dto);

    PersonalAccountFolderDto update(UUID folderUuid, PersonalAccountFolderDto dto);

    List<PersonalAccountFolderDto> getListOfCurrentAccountEntity();

    void delete(UUID folderUuid, FolderRemovalOptions removalOptions);
}
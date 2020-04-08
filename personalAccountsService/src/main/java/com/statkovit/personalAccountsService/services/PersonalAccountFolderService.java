package com.statkovit.personalAccountsService.services;

import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;

import java.util.List;
import java.util.UUID;

public interface PersonalAccountFolderService {

    PersonalAccountFolder save(PersonalAccountFolder folder);

    List<PersonalAccountFolder> getFolderListOfCurrentAccount();

    PersonalAccountFolder getByUuid(UUID uuid);
}

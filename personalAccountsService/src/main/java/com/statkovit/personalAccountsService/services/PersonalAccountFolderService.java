package com.statkovit.personalAccountsService.services;

import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;

import java.util.List;

public interface PersonalAccountFolderService {

    PersonalAccountFolder save(PersonalAccountFolder folder);

    List<PersonalAccountFolder> getFolderListOfCurrentAccount();
}

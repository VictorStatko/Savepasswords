package com.statkovit.personalAccountsService.rest;

import com.statkovit.personalAccountsService.payload.PersonalAccountFolderDto;

import java.util.List;

public interface PersonalAccountFolderRestService {

    PersonalAccountFolderDto create(PersonalAccountFolderDto dto);

    List<PersonalAccountFolderDto> getListOfCurrentAccountEntity();
}

package com.statkovit.personalAccountsService.rest;

import com.statkovit.personalAccountsService.payload.PersonalAccountDto;

import java.util.List;

public interface PersonalAccountRestService {

    PersonalAccountDto create(PersonalAccountDto personalAccountDto);

    List<PersonalAccountDto> getList();
}

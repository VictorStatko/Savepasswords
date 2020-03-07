package com.statkovit.personalAccountsService.rest;

import com.statkovit.personalAccountsService.payload.PersonalAccountDto;

public interface PersonalAccountRestService {

    PersonalAccountDto create(PersonalAccountDto personalAccountDto);
}

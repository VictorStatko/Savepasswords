package com.statkovit.personalAccountsService.rest;

import com.statkovit.personalAccountsService.payload.PersonalAccountDto;

import java.util.List;
import java.util.UUID;

public interface PersonalAccountRestService {

    PersonalAccountDto create(PersonalAccountDto personalAccountDto);

    void delete(UUID accountUuid);

    List<PersonalAccountDto> getList();
}

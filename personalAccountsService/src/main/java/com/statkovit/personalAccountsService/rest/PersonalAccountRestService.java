package com.statkovit.personalAccountsService.rest;

import com.statkovit.personalAccountsService.payload.LongDto;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.payload.filters.PersonalAccountListFilters;

import java.util.List;
import java.util.UUID;

public interface PersonalAccountRestService {

    PersonalAccountDto create(PersonalAccountDto personalAccountDto);

    PersonalAccountDto update(UUID accountUuid, PersonalAccountDto personalAccountDto);

    void delete(UUID accountUuid);

    List<PersonalAccountDto> getList(PersonalAccountListFilters filters);

    LongDto getListCount(PersonalAccountListFilters filters);
}

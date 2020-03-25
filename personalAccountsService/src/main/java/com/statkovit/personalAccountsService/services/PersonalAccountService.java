package com.statkovit.personalAccountsService.services;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;

import java.util.List;
import java.util.UUID;

public interface PersonalAccountService {

    PersonalAccount create(PersonalAccountDto personalAccountDto);

    void delete(UUID accountUuid);

    List<PersonalAccount> getList();
}

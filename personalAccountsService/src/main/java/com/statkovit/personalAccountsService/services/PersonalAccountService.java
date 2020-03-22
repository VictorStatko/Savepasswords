package com.statkovit.personalAccountsService.services;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;

import java.util.List;

public interface PersonalAccountService {

    PersonalAccount create(PersonalAccountDto personalAccountDto);

    List<PersonalAccount> getList();
}

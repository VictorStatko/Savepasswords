package com.statkovit.personalAccountsService.services;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;

public interface PersonalAccountService {

    PersonalAccount create(PersonalAccountDto personalAccountDto);
}

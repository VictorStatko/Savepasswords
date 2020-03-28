package com.statkovit.personalAccountsService.services;

import com.statkovit.personalAccountsService.domain.PersonalAccount;

import java.util.List;
import java.util.UUID;

public interface PersonalAccountService {

    PersonalAccount save(PersonalAccount personalAccount);

    void delete(UUID accountUuid);

    List<PersonalAccount> getList();

    PersonalAccount findOneByUuid(UUID accountUuid);
}

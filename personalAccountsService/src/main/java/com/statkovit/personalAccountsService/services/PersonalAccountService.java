package com.statkovit.personalAccountsService.services;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.statkovit.personalAccountsService.domain.PersonalAccount;

import java.util.List;
import java.util.UUID;

public interface PersonalAccountService {

    PersonalAccount save(PersonalAccount personalAccount);

    void delete(PersonalAccount personalAccount);

    List<PersonalAccount> getList(BooleanExpression booleanExpression);

    long count(BooleanExpression booleanExpression);

    PersonalAccount findOneByUuid(UUID accountUuid);
}

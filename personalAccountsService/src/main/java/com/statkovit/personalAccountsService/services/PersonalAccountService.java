package com.statkovit.personalAccountsService.services;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.repository.PersonalAccountRepository;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonalAccountService {

    private final PersonalAccountRepository personalAccountRepository;
    private final SecurityUtils securityUtils;

    @Transactional
    public PersonalAccount save(PersonalAccount personalAccount) {
        return personalAccountRepository.save(personalAccount);
    }

    @Transactional(readOnly = true)
    public List<PersonalAccount> getList(BooleanExpression booleanExpression) {
        return IterableUtils.toList(personalAccountRepository.findAll(booleanExpression));
    }

    @Transactional(readOnly = true)
    public long count(BooleanExpression booleanExpression) {
        return personalAccountRepository.count(booleanExpression);
    }

    @Transactional
    public void delete(PersonalAccount personalAccount) {
        personalAccountRepository.delete(personalAccount);
    }

    @Transactional(readOnly = true)
    public PersonalAccount findOneByUuid(UUID accountUuid) {
        Long accountEntityId = securityUtils.getCurrentAccountEntityId();

        return personalAccountRepository.findByUuidAndAccountEntityId(accountUuid, accountEntityId).orElseThrow(
                () -> new LocalizedException(
                        new EntityNotFoundException("Personal account with uuid = " + accountUuid + " has not been found."),
                        "exceptions.personalAccountNotFoundByUuid"
                )
        );
    }
}

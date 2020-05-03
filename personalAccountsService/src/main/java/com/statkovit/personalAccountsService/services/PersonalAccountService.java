package com.statkovit.personalAccountsService.services;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.domain.AccountData;
import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.payload.filters.PersonalAccountListFilters;
import com.statkovit.personalAccountsService.repository.PersonalAccountRepository;
import com.statkovit.personalAccountsService.repository.expressions.PersonalAccountsQueryBuilder;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonalAccountService {

    private final PersonalAccountRepository personalAccountRepository;
    private final SecurityUtils securityUtils;
    private final PersonalAccountsQueryBuilder expressionsBuilder;

    @Transactional
    public PersonalAccount save(PersonalAccount personalAccount) {
        return personalAccountRepository.save(personalAccount);
    }

    public List<PersonalAccount> getList(PersonalAccountListFilters filters) {
        JPAQuery<PersonalAccount> query = expressionsBuilder.createListQuery(filters);
        return query.fetch();
    }

    public long count(PersonalAccountListFilters filters) {
        JPAQuery<PersonalAccount> query = expressionsBuilder.createListQuery(filters);
        return query.fetchCount();
    }

    @Transactional
    public void delete(PersonalAccount personalAccount) {
        personalAccountRepository.delete(personalAccount);
    }

    @Transactional
    public void deleteAllByEntityId(Long accountEntityId) {
        personalAccountRepository.deleteAllByAccountEntityId(accountEntityId);
    }

    public PersonalAccount findOneByUuid(UUID accountUuid) {
        Long accountEntityId = securityUtils.getCurrentAccountEntityId();
        JPAQuery<PersonalAccount> query = expressionsBuilder.createUuidAccountEntityIdQuery(accountUuid, accountEntityId);

        return Optional.ofNullable(query.fetchOne())
                .orElseThrow(
                        () -> new LocalizedException(
                                new EntityNotFoundException("Personal account with uuid = " + accountUuid + " has not been found."),
                                "exceptions.personalAccountNotFoundByUuid"
                        )
                );
    }

    public List<Pair<AccountData, Long>> getSharing() {
        List<Tuple> tuples = expressionsBuilder.createSharingListQuery().fetch();

        return tuples.stream()
                .map(tuple -> {
                    AccountData accountData = tuple.get(0, AccountData.class);
                    Long sharingCounts = tuple.get(1, Long.class);
                    return Pair.of(accountData, sharingCounts);
                })
                .collect(Collectors.toList());
    }

}

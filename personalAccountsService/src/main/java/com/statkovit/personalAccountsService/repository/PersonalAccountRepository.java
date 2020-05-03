package com.statkovit.personalAccountsService.repository;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PersonalAccountRepository extends JpaRepository<PersonalAccount, Long>, QuerydslPredicateExecutor<PersonalAccount> {

    void deleteAllByAccountEntityId(Long accountEntityId);

}

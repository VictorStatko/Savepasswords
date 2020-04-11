package com.statkovit.personalAccountsService.repository;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;

public interface PersonalAccountRepository extends JpaRepository<PersonalAccount, Long>, QuerydslPredicateExecutor<PersonalAccount> {
    Optional<PersonalAccount> findByUuidAndAccountEntityId(UUID uuid, Long accountEntityId);
}

package com.statkovit.personalAccountsService.repository;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonalAccountRepository extends JpaRepository<PersonalAccount, Long> {

    List<PersonalAccount> findAllByAccountEntityId(Long accountEntityId);

    Optional<PersonalAccount> findByUuidAndAccountEntityId(UUID uuid, Long accountEntityId);
}

package com.statkovit.personalAccountsService.repository;

import com.statkovit.personalAccountsService.domain.AccountData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountDataRepository extends JpaRepository<AccountData, Long> {

    Optional<AccountData> findByUuid(UUID uuid);

    Optional<AccountData> findByEmail(String email);
}

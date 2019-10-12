package com.statkovit.userservice.repository;

import com.statkovit.userservice.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);

    Optional<Account> getByEmail(String email);

    Optional<Account> getByUuid(UUID uuid);
}

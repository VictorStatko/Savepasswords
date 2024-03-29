package com.statkovit.authorizationservice.repositories;

import com.statkovit.authorizationservice.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> getByEmail(String email);

    boolean existsByEmail(String email);
}

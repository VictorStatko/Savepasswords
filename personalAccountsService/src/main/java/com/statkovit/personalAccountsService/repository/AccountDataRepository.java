package com.statkovit.personalAccountsService.repository;

import com.statkovit.personalAccountsService.domain.AccountData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDataRepository extends JpaRepository<AccountData, Long> {
}

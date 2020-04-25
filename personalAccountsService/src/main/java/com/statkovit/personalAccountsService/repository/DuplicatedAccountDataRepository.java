package com.statkovit.personalAccountsService.repository;

import com.statkovit.personalAccountsService.domain.duplication.AccountData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DuplicatedAccountDataRepository extends JpaRepository<AccountData, Long> {
}

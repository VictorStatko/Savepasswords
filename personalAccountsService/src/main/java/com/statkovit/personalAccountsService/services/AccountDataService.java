package com.statkovit.personalAccountsService.services;

import com.statkovit.personalAccountsService.domain.duplication.AccountData;

public interface AccountDataService {
    AccountData save(AccountData accountData);
}

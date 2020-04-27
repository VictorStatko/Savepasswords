package com.statkovit.personalAccountsService.helpers.domain;

import com.statkovit.personalAccountsService.domain.AccountData;

import java.util.UUID;

public final class AccountDataDomainHelper {

    private static final String NN = "not null";
    private static final String EMAIL = "test@test.com";
    private static final Long DEFAULT_ID = 1L;
    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");

    public static AccountData.AccountDataBuilder<?, ?> prePopulatedValidAccountDataBuilder() {
        return
                AccountData
                        .builder()
                        .id(DEFAULT_ID)
                        .uuid(UUID_1)
                        .version(0L)
                        .publicKey(NN)
                        .email(EMAIL);
    }
}

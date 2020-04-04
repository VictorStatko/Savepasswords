package com.statkovit.personalAccountsService.helpers.domain;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;

public final class PersonalAccountDomainHelper {

    private static final String NN = "not null";
    private static final Long DEFAULT_ID = 1L;

    public static PersonalAccount account() {
        return new PersonalAccount();
    }

    public static PersonalAccountDto accountDto() {
        return new PersonalAccountDto();
    }

    public static PersonalAccountDto.PersonalAccountDtoBuilder<?, ?> prePopulatedValidAccountDtoBuilder() {
        return PersonalAccountDto.builder().url(NN);
    }

    public static PersonalAccount.PersonalAccountBuilder<?, ?> prePopulatedValidAccountBuilder() {
        return PersonalAccount.builder().accountEntityId(DEFAULT_ID).url(NN).fieldsEncryptionSalt(NN);
    }
}

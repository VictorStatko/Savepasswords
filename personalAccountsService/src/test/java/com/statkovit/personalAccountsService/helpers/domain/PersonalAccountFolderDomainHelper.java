package com.statkovit.personalAccountsService.helpers.domain;

import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.payload.PersonalAccountFolderDto;

public final class PersonalAccountFolderDomainHelper {

    private static final String NN = "not null";
    private static final Long DEFAULT_ID = 1L;

    private PersonalAccountFolderDomainHelper() {
    }

    public static PersonalAccountFolder folder() {
        return new PersonalAccountFolder();
    }

    public static PersonalAccountFolderDto folderDto() {
        return new PersonalAccountFolderDto();
    }

    public static PersonalAccountFolderDto.PersonalAccountFolderDtoBuilder<?, ?> prePopulatedValidFolderDtoBuilder() {
        return PersonalAccountFolderDto.builder().name(NN);
    }

    public static PersonalAccountFolder.PersonalAccountFolderBuilder<?, ?> prePopulatedValidFolderBuilder() {
        return PersonalAccountFolder.builder().name(NN).accountEntityId(DEFAULT_ID);
    }
}

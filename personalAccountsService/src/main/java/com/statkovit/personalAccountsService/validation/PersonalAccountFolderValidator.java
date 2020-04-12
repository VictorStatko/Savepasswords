package com.statkovit.personalAccountsService.validation;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.repository.PersonalAccountFolderRepository;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PersonalAccountFolderValidator {

    private final PersonalAccountFolderRepository personalAccountFolderRepository;
    private final SecurityUtils securityUtils;

    public void validateFolderExistence(String folderUuid) {
        final Long currentAccountEntityId = securityUtils.getCurrentAccountEntityId();
        UUID uuid;

        try {
            uuid = UUID.fromString(folderUuid);
        } catch (IllegalArgumentException e) {
            throw getFolderNotFoundException(folderUuid);
        }

        final boolean folderExists = personalAccountFolderRepository.existsByUuidAndAccountEntityId(uuid, currentAccountEntityId);

        if (!folderExists) {
            throw getFolderNotFoundException(folderUuid);
        }

    }

    private LocalizedException getFolderNotFoundException(String folderUuid) {
        return new LocalizedException(
                new EntityNotFoundException("Personal account folder with uuid = " + folderUuid + " has not been found."),
                "exceptions.personalAccountFolderNotFoundByUuid"
        );
    }
}

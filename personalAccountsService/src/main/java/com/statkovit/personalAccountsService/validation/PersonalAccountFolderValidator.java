package com.statkovit.personalAccountsService.validation;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.repository.PersonalAccountFolderRepository;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PersonalAccountFolderValidator {

    private final PersonalAccountFolderRepository personalAccountFolderRepository;
    private final SecurityUtils securityUtils;

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public void validateFolderUniqueName(String name, PersonalAccountFolder currentFolder) {
        Optional<PersonalAccountFolder> existingFolderOptional = personalAccountFolderRepository.findByNameAndAccountEntityId(
                name, currentFolder.getAccountEntityId()
        );

        if (existingFolderOptional.isPresent()) {
            PersonalAccountFolder existingFolder = existingFolderOptional.get();

            if (!existingFolder.getUuid().equals(currentFolder.getUuid())) {
                throw new LocalizedException(
                        String.format("Folder with name %s already exists.", name),
                        "exceptions.folderAlreadyExists"
                );
            }
        }
    }
}

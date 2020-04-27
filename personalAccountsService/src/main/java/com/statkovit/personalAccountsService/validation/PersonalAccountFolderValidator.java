package com.statkovit.personalAccountsService.validation;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.repository.PersonalAccountFolderRepository;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
public class PersonalAccountFolderValidator {

    private final PersonalAccountFolderRepository personalAccountFolderRepository;
    private final SecurityUtils securityUtils;

    public void validateFolderExistence(UUID folderUuid) {
        final Long currentAccountEntityId = securityUtils.getCurrentAccountEntityId();

        final boolean folderExists = personalAccountFolderRepository.existsByUuidAndAccountEntityId(folderUuid, currentAccountEntityId);

        if (!folderExists) {
            throw getFolderNotFoundException(folderUuid);
        }

    }

    private LocalizedException getFolderNotFoundException(UUID folderUuid) {
        return new LocalizedException(
                new EntityNotFoundException("Personal account folder with uuid = " + folderUuid + " has not been found."),
                "exceptions.personalAccountFolderNotFoundByUuid"
        );
    }

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

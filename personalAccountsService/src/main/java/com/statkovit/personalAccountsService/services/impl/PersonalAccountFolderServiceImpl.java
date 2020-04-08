package com.statkovit.personalAccountsService.services.impl;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.repository.PersonalAccountFolderRepository;
import com.statkovit.personalAccountsService.services.PersonalAccountFolderService;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonalAccountFolderServiceImpl implements PersonalAccountFolderService {

    private final PersonalAccountFolderRepository personalAccountFolderRepository;
    private final SecurityUtils securityUtils;

    @Override
    public PersonalAccountFolder save(PersonalAccountFolder folder) {

        Optional<PersonalAccountFolder> existingFolderOptional = personalAccountFolderRepository.findByNameAndAccountEntityId(
                folder.getName(), folder.getAccountEntityId()
        );

        if (existingFolderOptional.isPresent()) {
            PersonalAccountFolder existingFolder = existingFolderOptional.get();

            if (!existingFolder.getUuid().equals(folder.getUuid())) {
                throw new LocalizedException(
                        String.format("Folder with name %s already exists.", folder.getName()),
                        "exceptions.folderAlreadyExists"
                );
            }
        }

        return personalAccountFolderRepository.save(folder);
    }

    @Override
    public List<PersonalAccountFolder> getFolderListOfCurrentAccount() {
        Long currentAccountEntityId = securityUtils.getCurrentAccountEntityId();

        return personalAccountFolderRepository.findAllByAccountEntityId(currentAccountEntityId);
    }

    @Override
    public PersonalAccountFolder getByUuid(UUID uuid) {
        Long accountEntityId = securityUtils.getCurrentAccountEntityId();

        return personalAccountFolderRepository.findByUuidAndAccountEntityId(uuid, accountEntityId).orElseThrow(
                () -> new LocalizedException(
                        new EntityNotFoundException("Personal account folder with uuid = " + uuid + " has not been found."),
                        "exceptions.personalAccountFolderNotFoundByUuid"
                )
        );
    }
}

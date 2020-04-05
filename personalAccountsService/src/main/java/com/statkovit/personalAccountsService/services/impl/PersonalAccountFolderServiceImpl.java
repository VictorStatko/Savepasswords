package com.statkovit.personalAccountsService.services.impl;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.repository.PersonalAccountFolderRepository;
import com.statkovit.personalAccountsService.services.PersonalAccountFolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonalAccountFolderServiceImpl implements PersonalAccountFolderService {

    private final PersonalAccountFolderRepository personalAccountFolderRepository;

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
}

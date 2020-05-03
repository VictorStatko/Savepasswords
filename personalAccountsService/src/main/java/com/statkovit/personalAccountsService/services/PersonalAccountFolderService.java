package com.statkovit.personalAccountsService.services;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.enums.FolderRemovalOptions;
import com.statkovit.personalAccountsService.repository.PersonalAccountFolderRepository;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonalAccountFolderService {

    private final PersonalAccountFolderRepository personalAccountFolderRepository;
    private final SecurityUtils securityUtils;
    private final PersonalAccountService personalAccountService;

    @Transactional
    public PersonalAccountFolder save(PersonalAccountFolder folder) {
        return personalAccountFolderRepository.save(folder);
    }

    public List<PersonalAccountFolder> getFolderListOfCurrentAccount() {
        Long currentAccountEntityId = securityUtils.getCurrentAccountEntityId();

        return personalAccountFolderRepository.findAllByAccountEntityId(currentAccountEntityId);
    }

    public PersonalAccountFolder getByUuid(UUID uuid) {
        Long accountEntityId = securityUtils.getCurrentAccountEntityId();

        return personalAccountFolderRepository.findByUuidAndAccountEntityId(uuid, accountEntityId).orElseThrow(
                () -> new LocalizedException(
                        new EntityNotFoundException("Personal account folder with uuid = " + uuid + " has not been found."),
                        "exceptions.personalAccountFolderNotFoundByUuid"
                )
        );
    }

    @Transactional
    public void remove(PersonalAccountFolder folder, FolderRemovalOptions removalOptions) {
        switch (removalOptions) {
            case FOLDER_ONLY:
                folder.getAccounts().forEach(personalAccount -> {
                    personalAccount.setFolder(null);
                    personalAccountService.save(personalAccount);
                });
                break;
            case WITH_ACCOUNTS:
                folder.getAccounts().forEach(personalAccountService::delete);
                break;
            default:
                throw new IllegalArgumentException("Unknown removal option");
        }

        personalAccountFolderRepository.delete(folder);
    }

    @Transactional
    public void removeAllByAccountEntityId(Long accountEntityId) {
        personalAccountFolderRepository.deleteAllByAccountEntityId(accountEntityId);
    }
}

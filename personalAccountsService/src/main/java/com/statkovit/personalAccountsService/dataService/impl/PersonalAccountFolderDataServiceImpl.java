package com.statkovit.personalAccountsService.dataService.impl;

import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.enums.FolderRemovalOptions;
import com.statkovit.personalAccountsService.payload.PersonalAccountFolderDto;
import com.statkovit.personalAccountsService.payload.converters.PersonalAccountFolderConverter;
import com.statkovit.personalAccountsService.dataService.PersonalAccountFolderDataService;
import com.statkovit.personalAccountsService.services.PersonalAccountFolderService;
import com.statkovit.personalAccountsService.validation.PersonalAccountFolderValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonalAccountFolderDataServiceImpl implements PersonalAccountFolderDataService {

    private final PersonalAccountFolderConverter folderConverter;
    private final PersonalAccountFolderService personalAccountFolderService;
    private final PersonalAccountFolderValidator personalAccountFolderValidator;

    @Override
    public PersonalAccountFolderDto create(PersonalAccountFolderDto dto) {
        PersonalAccountFolder folderForSave = new PersonalAccountFolder();

        folderConverter.toEntity(dto, folderForSave);

        personalAccountFolderValidator.validateFolderUniqueName(dto.getName(), folderForSave);

        folderForSave = personalAccountFolderService.save(folderForSave);

        return folderConverter.toDto(folderForSave);
    }

    @Override
    public PersonalAccountFolderDto update(UUID folderUuid, PersonalAccountFolderDto dto) {
        PersonalAccountFolder folderToUpdate = personalAccountFolderService.getByUuid(folderUuid);

        folderConverter.toEntity(dto, folderToUpdate);

        personalAccountFolderValidator.validateFolderUniqueName(dto.getName(), folderToUpdate);

        folderToUpdate = personalAccountFolderService.save(folderToUpdate);

        return folderConverter.toDto(folderToUpdate);
    }

    @Override
    public List<PersonalAccountFolderDto> getListOfCurrentAccountEntity() {
        List<PersonalAccountFolder> folders = personalAccountFolderService.getFolderListOfCurrentAccount();

        return folders.stream().map(folderConverter::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(UUID folderUuid, FolderRemovalOptions removalOptions) {
        PersonalAccountFolder folderToRemove = personalAccountFolderService.getByUuid(folderUuid);

        personalAccountFolderService.remove(folderToRemove, removalOptions);
    }
}

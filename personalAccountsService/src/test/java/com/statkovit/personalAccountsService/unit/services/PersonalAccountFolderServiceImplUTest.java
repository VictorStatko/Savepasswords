package com.statkovit.personalAccountsService.unit.services;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.repository.PersonalAccountFolderRepository;
import com.statkovit.personalAccountsService.services.impl.PersonalAccountFolderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalAccountFolderServiceImplUTest {

    @Mock
    private PersonalAccountFolderRepository personalAccountFolderRepository;

    @InjectMocks
    private PersonalAccountFolderServiceImpl personalAccountFolderService;

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Test
    void save_shouldCallRepositoryMethod() {
        final PersonalAccountFolder folderMock = PersonalAccountFolder.builder()
                .uuid(UUID_1).build();

        personalAccountFolderService.save(folderMock);

        verify(personalAccountFolderRepository, times(1)).save(folderMock);
    }

    @Test
    void save_shouldSaveOnlyUniqueNamesForUserAccount() {
        final PersonalAccountFolder existingMock = PersonalAccountFolder.builder()
                .uuid(UUID_1)
                .accountEntityId(1L)
                .name("name")
                .build();

        when(personalAccountFolderRepository.findByNameAndAccountEntityId("name", 1L)).thenReturn(Optional.of(existingMock));
        when(personalAccountFolderRepository.findByNameAndAccountEntityId(AdditionalMatchers.not(eq("name")), eq(1L))).thenReturn(Optional.empty());

        final PersonalAccountFolder folderMock1 = PersonalAccountFolder.builder()
                .accountEntityId(1L)
                .name("name")
                .build();

        Assertions.assertThrows(LocalizedException.class, () -> personalAccountFolderService.save(folderMock1));

        final PersonalAccountFolder folderMock2 = PersonalAccountFolder.builder()
                .accountEntityId(1L)
                .name("name1")
                .build();

        Assertions.assertDoesNotThrow(() -> personalAccountFolderService.save(folderMock2));

        final PersonalAccountFolder folderMock3 = PersonalAccountFolder.builder()
                .accountEntityId(1L)
                .uuid(UUID_1)
                .name("name")
                .build();

        Assertions.assertDoesNotThrow(() -> personalAccountFolderService.save(folderMock3));
    }

}
package com.statkovit.personalAccountsService.unit.services;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.enums.FolderRemovalOptions;
import com.statkovit.personalAccountsService.repository.PersonalAccountFolderRepository;
import com.statkovit.personalAccountsService.services.PersonalAccountFolderService;
import com.statkovit.personalAccountsService.services.PersonalAccountService;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalAccountFolderServiceUTest {

    @Mock
    private PersonalAccountFolderRepository personalAccountFolderRepository;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private PersonalAccountService personalAccountService;

    @InjectMocks
    private PersonalAccountFolderService personalAccountFolderService;

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID UUID_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final UUID UUID_3 = UUID.fromString("00000000-0000-0000-0000-000000000003");

    @Test
    void save_shouldCallRepositoryMethod() {
        final PersonalAccountFolder folderMock = PersonalAccountFolder.builder()
                .uuid(UUID_1).build();

        personalAccountFolderService.save(folderMock);

        verify(personalAccountFolderRepository, times(1)).save(folderMock);
    }

    @Test
    void getListShouldReturnFoldersOfCurrentEntityId() {
        PersonalAccountFolder firstAccountFolder = PersonalAccountFolder.builder()
                .uuid(UUID_1).accountEntityId(1L).build();

        PersonalAccountFolder secondAccountFolder = PersonalAccountFolder.builder()
                .uuid(UUID_2).accountEntityId(2L).build();

        List<PersonalAccountFolder> allAccountFolders = List.of(firstAccountFolder, secondAccountFolder);

        when(personalAccountFolderRepository.findAllByAccountEntityId(anyLong()))
                .then(invocation -> {
                    Long accountEntityId = invocation.getArgument(0);
                    return allAccountFolders.stream().filter(folder -> accountEntityId.equals(folder.getAccountEntityId())).collect(Collectors.toList());
                });

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(1L);

        List<PersonalAccountFolder> result = personalAccountFolderService.getFolderListOfCurrentAccount();
        assertEquals(1, result.size());
        assertEquals(firstAccountFolder, result.get(0));

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(2L);

        result = personalAccountFolderService.getFolderListOfCurrentAccount();
        assertEquals(1, result.size());
        assertSame(secondAccountFolder, result.get(0));

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(3L);

        result = personalAccountFolderService.getFolderListOfCurrentAccount();
        assertEquals(0, result.size());
    }

    @Test
    void getByUuidShouldReturnAccountIfCorrectData() {
        PersonalAccountFolder firstAccountFolder = PersonalAccountFolder.builder()
                .uuid(UUID_1).accountEntityId(1L).build();

        PersonalAccountFolder secondAccountFolder = PersonalAccountFolder.builder()
                .uuid(UUID_2).accountEntityId(2L).build();

        List<PersonalAccountFolder> accountFolders = List.of(firstAccountFolder, secondAccountFolder);

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(2L);

        when(personalAccountFolderRepository.findByUuidAndAccountEntityId(any(UUID.class), anyLong()))
                .then(invocation -> {
                    UUID uuid = invocation.getArgument(0);
                    Long accountEntityId = invocation.getArgument(1);
                    return accountFolders.stream().filter(folder ->
                            uuid.equals(folder.getUuid())
                                    && accountEntityId.equals(folder.getAccountEntityId())
                    ).findFirst();
                });

        final PersonalAccountFolder result = personalAccountFolderService.getByUuid(UUID_2);
        assertNotNull(result);
        assertEquals(result.getUuid(), UUID_2);
    }

    @Test
    void getByUuidShouldThrowLocalizedExceptionIfIncorrectData() {
        PersonalAccountFolder firstAccountFolder = PersonalAccountFolder.builder()
                .uuid(UUID_1).accountEntityId(1L).build();

        PersonalAccountFolder secondAccountFolder = PersonalAccountFolder.builder()
                .uuid(UUID_2).accountEntityId(2L).build();

        List<PersonalAccountFolder> accountFolders = List.of(firstAccountFolder, secondAccountFolder);

        when(securityUtils.getCurrentAccountEntityId()).thenReturn(2L);

        when(personalAccountFolderRepository.findByUuidAndAccountEntityId(any(UUID.class), anyLong()))
                .then(invocation -> {
                    UUID uuid = invocation.getArgument(0);
                    Long accountEntityId = invocation.getArgument(1);
                    return accountFolders.stream().filter(folder ->
                            uuid.equals(folder.getUuid())
                                    && accountEntityId.equals(folder.getAccountEntityId())
                    ).findFirst();
                });

        Exception exception = assertThrows(LocalizedException.class, () ->
                personalAccountFolderService.getByUuid(UUID_1)
        );

        assertTrue(exception.getCause() instanceof EntityNotFoundException);
    }

    @Test
    void removeShouldWorkAccordingToFolderOnlyRemovalOption() {
        PersonalAccount account1 = PersonalAccount.builder().uuid(UUID_1).build();
        PersonalAccount account2 = PersonalAccount.builder().uuid(UUID_2).build();

        PersonalAccountFolder folder = PersonalAccountFolder.builder().uuid(UUID_3).accounts(List.of(account1, account2)).build();

        account1.setFolder(folder);
        account2.setFolder(folder);

        personalAccountFolderService.remove(folder, FolderRemovalOptions.FOLDER_ONLY);

        verify(personalAccountService, times(1)).save(account1);
        verify(personalAccountService, times(1)).save(account2);
        verify(personalAccountService, times(0)).delete(account1);
        verify(personalAccountService, times(0)).delete(account2);

        assertNull(account1.getFolder());
        assertNull(account2.getFolder());

        verify(personalAccountFolderRepository, times(1)).delete(folder);
    }

    @Test
    void removeShouldWorkAccordingToWithAccountsRemovalOption() {
        PersonalAccount account1 = PersonalAccount.builder().uuid(UUID_1).build();
        PersonalAccount account2 = PersonalAccount.builder().uuid(UUID_2).build();

        PersonalAccountFolder folder = PersonalAccountFolder.builder().uuid(UUID_3).accounts(List.of(account1, account2)).build();

        personalAccountFolderService.remove(folder, FolderRemovalOptions.WITH_ACCOUNTS);

        verify(personalAccountService, times(1)).delete(account1);
        verify(personalAccountService, times(1)).delete(account2);

        verify(personalAccountFolderRepository, times(1)).delete(folder);
    }
}
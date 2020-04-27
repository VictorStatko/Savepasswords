package com.statkovit.personalAccountsService.unit.payload.converters;

import com.statkovit.personalAccountsService.domain.AccountData;
import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.payload.PersonalAccountFolderDto;
import com.statkovit.personalAccountsService.payload.converters.PersonalAccountFolderConverter;
import com.statkovit.personalAccountsService.payload.mappers.PersonalAccountFolderMapper;
import com.statkovit.personalAccountsService.services.AccountDataService;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountFolderDomainHelper.folder;
import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountFolderDomainHelper.folderDto;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PersonalAccountFolderConverterUTest {

    @Mock
    private PersonalAccountFolderMapper personalAccountFolderMapper;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private AccountDataService accountDataService;

    @InjectMocks
    private PersonalAccountFolderConverter personalAccountFolderConverter;

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Test
    void toEntityConverterShouldSetAccountEntityIdForNewFolder() {
        final PersonalAccountFolder accountFolderForUpdate = folder();
        final PersonalAccountFolderDto dtoForUpdate = folderDto();

        Mockito.when(securityUtils.getCurrentAccountEntityId()).thenReturn(1L);
        Mockito.when(accountDataService.internalGetById(1L)).thenReturn(new AccountData().toBuilder().id(1L).build());

        personalAccountFolderConverter.toEntity(dtoForUpdate, accountFolderForUpdate);

        Assertions.assertEquals(1L, accountFolderForUpdate.getAccountEntityId());
        Assertions.assertEquals(1L, accountFolderForUpdate.getDuplicatedAccountEntity().getId());
    }

    @Test
    void toEntityConverterShouldNotUpdateAccountEntityIdForOldFolder() {
        final PersonalAccountFolder folderForUpdate = PersonalAccountFolder.builder()
                .uuid(UUID_1).accountEntityId(1L).build();

        final PersonalAccountFolderDto dtoForUpdate = PersonalAccountFolderDto.builder()
                .uuid(UUID_1).build();

        personalAccountFolderConverter.toEntity(dtoForUpdate, folderForUpdate);

        Assertions.assertEquals(1L, folderForUpdate.getAccountEntityId());
    }

    @Test
    void toEntityConverterShouldCallEntityMapper() {
        final PersonalAccountFolder folderForUpdate = PersonalAccountFolder.builder()
                .uuid(UUID_1).build();

        final PersonalAccountFolderDto dtoForUpdate = PersonalAccountFolderDto.builder()
                .uuid(UUID_1).build();

        personalAccountFolderConverter.toEntity(dtoForUpdate, folderForUpdate);

        verify(personalAccountFolderMapper, times(1)).toEntity(dtoForUpdate, folderForUpdate);
    }

    @Test
    void toDtoConverterShouldReturnEntityFromEntityMapper() {
        final PersonalAccountFolder accountFolderForMapping = PersonalAccountFolder.builder()
                .uuid(UUID_1).build();

        final PersonalAccountFolderDto dtoAfterMapping = PersonalAccountFolderDto.builder()
                .uuid(UUID_1).build();

        Mockito.when(personalAccountFolderMapper.toDto(accountFolderForMapping)).thenReturn(dtoAfterMapping);

        final PersonalAccountFolderDto resultDto = personalAccountFolderConverter.toDto(accountFolderForMapping);

        Assertions.assertEquals(resultDto, dtoAfterMapping);
    }
}
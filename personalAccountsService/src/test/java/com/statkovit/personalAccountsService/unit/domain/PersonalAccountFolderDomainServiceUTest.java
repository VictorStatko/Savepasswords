package com.statkovit.personalAccountsService.unit.domain;

import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.domainService.PersonalAccountFolderDomainService;
import com.statkovit.personalAccountsService.enums.FolderRemovalOptions;
import com.statkovit.personalAccountsService.payload.PersonalAccountFolderDto;
import com.statkovit.personalAccountsService.payload.converters.PersonalAccountFolderConverter;
import com.statkovit.personalAccountsService.services.PersonalAccountFolderService;
import com.statkovit.personalAccountsService.validation.PersonalAccountFolderValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountFolderDomainHelper.folderDto;
import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountFolderDomainHelper.prePopulatedValidFolderBuilder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalAccountFolderDomainServiceUTest {

    @Mock
    private PersonalAccountFolderConverter folderConverter;

    @Mock
    private PersonalAccountFolderService personalAccountFolderService;

    @Mock
    private PersonalAccountFolderValidator folderValidator;

    @InjectMocks
    private PersonalAccountFolderDomainService personalAccountFolderRestService;

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID UUID_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @Test
    void create_shouldReturnDtoOfSavedFolder() {
        final PersonalAccountFolderDto dtoBeforeSave = folderDto();

        final PersonalAccountFolderDto dtoAfterSave = PersonalAccountFolderDto.builder().uuid(UUID_1).build();
        final PersonalAccountFolder folderAfterSave = PersonalAccountFolder.builder().uuid(UUID_1).build();

        when(personalAccountFolderService.save(any(PersonalAccountFolder.class))).thenReturn(folderAfterSave);
        when(folderConverter.toDto(folderAfterSave)).thenReturn(dtoAfterSave);

        PersonalAccountFolderDto resultDto = personalAccountFolderRestService.create(dtoBeforeSave);

        Assertions.assertEquals(dtoAfterSave, resultDto);
    }

    @Test
    void create_shouldCallConverterToEntity() {
        final PersonalAccountFolderDto dto = folderDto();

        personalAccountFolderRestService.create(dto);

        verify(folderConverter, times(1)).toEntity(same(dto), any(PersonalAccountFolder.class));
    }

    @Test
    void create_shouldCallNameValidator() {
        final PersonalAccountFolderDto dto = PersonalAccountFolderDto.builder().name("name").build();

        personalAccountFolderRestService.create(dto);

        verify(folderValidator, times(1)).validateFolderUniqueName(same("name"), any(PersonalAccountFolder.class));
    }

    @Test
    void update_shouldReturnDtoOfSavedFolder() {
        final PersonalAccountFolder folderBeforeSave = prePopulatedValidFolderBuilder().uuid(UUID_1).build();
        final PersonalAccountFolderDto dtoBeforeSave = folderDto();

        final PersonalAccountFolderDto dtoAfterSave = PersonalAccountFolderDto.builder().uuid(UUID_1).build();
        final PersonalAccountFolder folderAfterSave = PersonalAccountFolder.builder().uuid(UUID_1).build();

        when(personalAccountFolderService.getByUuid(UUID_1)).thenReturn(folderBeforeSave);

        when(personalAccountFolderService.save(folderBeforeSave)).thenReturn(folderAfterSave);
        when(folderConverter.toDto(folderAfterSave)).thenReturn(dtoAfterSave);

        PersonalAccountFolderDto resultDto = personalAccountFolderRestService.update(UUID_1, dtoBeforeSave);

        Assertions.assertEquals(dtoAfterSave, resultDto);
    }

    @Test
    void update_shouldCallConverterToEntity() {
        final PersonalAccountFolderDto dtoForUpdate = PersonalAccountFolderDto.builder().uuid(UUID_1).build();
        final PersonalAccountFolder folderForUpdate = PersonalAccountFolder.builder().uuid(UUID_1).build();

        when(personalAccountFolderService.getByUuid(UUID_1)).thenReturn(folderForUpdate);

        personalAccountFolderRestService.update(UUID_1, dtoForUpdate);

        verify(folderConverter, times(1)).toEntity(dtoForUpdate, folderForUpdate);
    }

    @Test
    void update_shouldCallNameValidator() {
        final PersonalAccountFolderDto dtoForUpdate = PersonalAccountFolderDto.builder().uuid(UUID_1).name("name").build();
        final PersonalAccountFolder folderForUpdate = PersonalAccountFolder.builder().uuid(UUID_1).build();

        when(personalAccountFolderService.getByUuid(UUID_1)).thenReturn(folderForUpdate);

        personalAccountFolderRestService.update(UUID_1, dtoForUpdate);

        verify(folderValidator, times(1)).validateFolderUniqueName("name", folderForUpdate);
    }

    @Test
    void getListShouldReturnListOfDtos() {
        PersonalAccountFolder firstAccountFolder = PersonalAccountFolder.builder().uuid(UUID_1).build();
        PersonalAccountFolderDto firstAccountFolderDto = PersonalAccountFolderDto.builder().uuid(UUID_1).build();
        PersonalAccountFolder secondAccountFolder = PersonalAccountFolder.builder().uuid(UUID_2).build();
        PersonalAccountFolderDto secondAccountFolderDto = PersonalAccountFolderDto.builder().uuid(UUID_2).build();

        List<PersonalAccountFolder> accountFolders = List.of(firstAccountFolder, secondAccountFolder);

        when(personalAccountFolderService.getFolderListOfCurrentAccount()).thenReturn(accountFolders);

        when(folderConverter.toDto(any(PersonalAccountFolder.class))).thenAnswer(invocation -> {
            PersonalAccountFolder param = invocation.getArgument(0);
            if (param == firstAccountFolder) {
                return firstAccountFolderDto;
            }
            if (param == secondAccountFolder) {
                return secondAccountFolderDto;
            }
            throw new RuntimeException("Invalid account");
        });

        List<PersonalAccountFolderDto> resultDtos = personalAccountFolderRestService.getListOfCurrentAccountEntity();

        Assertions.assertEquals(resultDtos.size(), 2);
        Assertions.assertEquals(resultDtos.get(0), firstAccountFolderDto);
        Assertions.assertEquals(resultDtos.get(1), secondAccountFolderDto);
    }

    @Test
    void deleteShouldCallServiceMethodExactlyOnce() {
        final PersonalAccountFolder folderForRemove = PersonalAccountFolder.builder().uuid(UUID_1).build();
        final FolderRemovalOptions anyOption = Mockito.mock(FolderRemovalOptions.class);

        when(personalAccountFolderService.getByUuid(UUID_1)).thenReturn(folderForRemove);

        personalAccountFolderRestService.delete(UUID_1, anyOption);

        verify(personalAccountFolderService, times(1)).remove(folderForRemove, anyOption);
    }
}
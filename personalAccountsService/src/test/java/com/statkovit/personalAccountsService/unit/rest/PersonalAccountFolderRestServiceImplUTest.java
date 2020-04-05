package com.statkovit.personalAccountsService.unit.rest;

import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.payload.PersonalAccountFolderDto;
import com.statkovit.personalAccountsService.payload.converters.PersonalAccountFolderConverter;
import com.statkovit.personalAccountsService.rest.impl.PersonalAccountFolderRestServiceImpl;
import com.statkovit.personalAccountsService.services.PersonalAccountFolderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountFolderDomainHelper.folderDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalAccountFolderRestServiceImplUTest {

    @Mock
    private PersonalAccountFolderConverter folderConverter;

    @Mock
    private PersonalAccountFolderService personalAccountFolderService;

    @InjectMocks
    private PersonalAccountFolderRestServiceImpl personalAccountFolderRestService;

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");

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


}
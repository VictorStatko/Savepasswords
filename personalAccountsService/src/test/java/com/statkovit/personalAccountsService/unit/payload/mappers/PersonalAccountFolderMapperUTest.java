package com.statkovit.personalAccountsService.unit.payload.mappers;

import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.payload.PersonalAccountFolderDto;
import com.statkovit.personalAccountsService.payload.mappers.PersonalAccountFolderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountFolderDomainHelper.folder;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PersonalAccountFolderMapperUTest {

    private PersonalAccountFolderMapper personalAccountFolderMapper;

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final String NAME = "name";

    @BeforeEach
    void beforeEach() {
        personalAccountFolderMapper = new PersonalAccountFolderMapper();
    }

    @Test
    void shouldMapDtoToEntity() {
        PersonalAccountFolderDto dto = PersonalAccountFolderDto.builder()
                .name(NAME)
                .uuid(UUID_1)
                .build();

        PersonalAccountFolder folder = folder();
        personalAccountFolderMapper.toEntity(dto, folder);

        assertNull(folder.getUuid());

        assertEquals(dto.getName(), folder.getName());
    }

    @Test
    void shouldMapEntityToDto() {
        PersonalAccountFolder folder = PersonalAccountFolder.builder()
                .name(NAME)
                .uuid(UUID_1)
                .build();

        PersonalAccountFolderDto dto = personalAccountFolderMapper.toDto(folder);

        assertNotNull(dto);

        assertEquals(folder.getName(), dto.getName());
        assertEquals(folder.getUuid(), dto.getUuid());
    }

}
package com.statkovit.personalAccountsService.unit.payload.mappers;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.payload.mappers.PersonalAccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountDomainHelper.account;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PersonalAccountMapperUTest {

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID UUID_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String URL = "url";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";

    private PersonalAccountMapper personalAccountMapper;

    @BeforeEach
    void beforeEach() {
        personalAccountMapper = new PersonalAccountMapper();
    }

    @Test
    void shouldMapDtoToEntity() {
        PersonalAccountDto dto = PersonalAccountDto.builder()
                .uuid(UUID_1)
                .password(PASSWORD)
                .username(USERNAME)
                .url(URL)
                .name(NAME)
                .description(DESCRIPTION)
                .folderUuid(UUID_2)
                .build();

        PersonalAccount account = account();
        personalAccountMapper.toEntity(dto, account);

        assertNotNull(account);
        assertNull(account.getUuid());
        assertNull(account.getFolder());

        assertEquals(dto.getPassword(), account.getPassword());
        assertEquals(dto.getUsername(), account.getUsername());
        assertEquals(dto.getUrl(), account.getUrl());
        assertEquals(dto.getName(), account.getName());
        assertEquals(dto.getDescription(), account.getDescription());
    }

    @Test
    void shouldMapEntityToDTO() {
        PersonalAccount account = PersonalAccount.builder()
                .uuid(UUID_1)
                .password(PASSWORD)
                .username(USERNAME)
                .url(URL)
                .name(NAME)
                .description(DESCRIPTION)
                .folder(PersonalAccountFolder.builder().uuid(UUID_2).build())
                .build();

        PersonalAccountDto dto = personalAccountMapper.toDto(account);

        assertNotNull(dto);

        assertEquals(account.getUuid(), dto.getUuid());
        assertEquals(account.getPassword(), dto.getPassword());
        assertEquals(account.getUsername(), dto.getUsername());
        assertEquals(account.getUrl(), dto.getUrl());
        assertEquals(account.getName(), dto.getName());
        assertEquals(account.getDescription(), dto.getDescription());
        assertEquals(account.getFolder().getUuid(), dto.getFolderUuid());
    }
}
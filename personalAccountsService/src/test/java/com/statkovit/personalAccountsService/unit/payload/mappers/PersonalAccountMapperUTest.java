package com.statkovit.personalAccountsService.unit.payload.mappers;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.payload.mappers.PersonalAccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static com.statkovit.personalAccountsService.unit.helper.domain.PersonalAccountDomainHelper.account;
import static com.statkovit.personalAccountsService.unit.helper.domain.PersonalAccountDomainHelper.accountDTO;

@ExtendWith(MockitoExtension.class)
class PersonalAccountMapperUTest {

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
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
        PersonalAccountDto dto = accountDTO();
        dto.setUuid(UUID_1);
        dto.setPassword(PASSWORD);
        dto.setUsername(USERNAME);
        dto.setUrl(URL);
        dto.setName(NAME);
        dto.setDescription(DESCRIPTION);

        PersonalAccount account = account();
        personalAccountMapper.toEntity(dto, account);

        assertNotNull(account);
        assertNull(account.getUuid());

        assertEquals(dto.getPassword(), account.getPassword());
        assertEquals(dto.getUsername(), account.getUsername());
        assertEquals(dto.getUrl(), account.getUrl());
        assertEquals(dto.getName(), account.getName());
        assertEquals(dto.getDescription(), account.getDescription());
    }

    @Test
    void shouldMapEntityToDTO() {
        PersonalAccount account = account();
        account.setUuid(UUID_1);
        account.setPassword(PASSWORD);
        account.setUsername(USERNAME);
        account.setUrl(URL);
        account.setName(NAME);
        account.setDescription(DESCRIPTION);

        PersonalAccountDto dto = personalAccountMapper.toDto(account);

        assertNotNull(dto);

        assertEquals(account.getUuid(), dto.getUuid());
        assertEquals(account.getPassword(), dto.getPassword());
        assertEquals(account.getUsername(), dto.getUsername());
        assertEquals(account.getUrl(), dto.getUrl());
        assertEquals(account.getName(), dto.getName());
        assertEquals(account.getDescription(), dto.getDescription());
    }
}
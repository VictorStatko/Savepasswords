package com.statkovit.personalAccountsService.unit.validation;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.repository.PersonalAccountFolderRepository;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import com.statkovit.personalAccountsService.validation.PersonalAccountFolderValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonalAccountFolderValidatorUTest {

    @Mock
    SecurityUtils securityUtils;

    @Mock
    PersonalAccountFolderRepository folderRepository;

    @InjectMocks
    PersonalAccountFolderValidator folderValidator;

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Test
    void validateFolderExistence_shouldThrowExceptionIfFolderNotExists() {
        when(securityUtils.getCurrentAccountEntityId()).thenReturn(1L);
        when(folderRepository.existsByUuidAndAccountEntityId(UUID_1, 1L)).thenReturn(false);


        Exception exception = assertThrows(LocalizedException.class, () ->
                folderValidator.validateFolderExistence(UUID_1.toString())
        );

        assertTrue(exception.getCause() instanceof EntityNotFoundException);
    }

    @Test
    void validateFolderExistence_shouldThrowExceptionIfFolderUuidInvalid() {
        Exception exception = assertThrows(LocalizedException.class, () ->
                folderValidator.validateFolderExistence("invalidUuid")
        );

        assertTrue(exception.getCause() instanceof EntityNotFoundException);
    }

    @Test
    void validateFolderExistence_shouldNotThrowExceptionIfFolderExists() {
        when(securityUtils.getCurrentAccountEntityId()).thenReturn(1L);
        when(folderRepository.existsByUuidAndAccountEntityId(UUID_1, 1L)).thenReturn(true);


        assertDoesNotThrow(() ->
                folderValidator.validateFolderExistence(UUID_1.toString())
        );

    }
}
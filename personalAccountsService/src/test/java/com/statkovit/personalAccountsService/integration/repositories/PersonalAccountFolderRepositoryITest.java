package com.statkovit.personalAccountsService.integration.repositories;

import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.repository.PersonalAccountFolderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountFolderDomainHelper.prePopulatedValidFolderBuilder;


class PersonalAccountFolderRepositoryITest extends BaseRepositoryTest {

    @Autowired
    private PersonalAccountFolderRepository personalAccountFolderRepository;

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID UUID_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @Test
    void save_nameCanNotBeNull() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                personalAccountFolderRepository.saveAndFlush(
                        prePopulatedValidFolderBuilder().name(null).build()
                )
        );
    }

    @Test
    void save_nameCanNotExceedLength() {
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                personalAccountFolderRepository.saveAndFlush(
                        prePopulatedValidFolderBuilder().name("-".repeat(PersonalAccountFolder.MAX_LENGTH__NAME + 1)).build()
                )
        );
    }

    @Test
    void findByNameAndAccountEntityId_ShouldReturnOnlyRequiredEntities() {
        personalAccountFolderRepository.saveAndFlush(
                prePopulatedValidFolderBuilder().accountEntityId(1L).name("name1").build()
        );

        personalAccountFolderRepository.saveAndFlush(
                prePopulatedValidFolderBuilder().accountEntityId(1L).name("name2").build()
        );

        Optional<PersonalAccountFolder> optional = personalAccountFolderRepository.findByNameAndAccountEntityId("name1", 1L);

        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(1L, optional.get().getAccountEntityId());
        Assertions.assertEquals("name1", optional.get().getName());

        optional = personalAccountFolderRepository.findByNameAndAccountEntityId("name2", 1L);

        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(1L, optional.get().getAccountEntityId());
        Assertions.assertEquals("name2", optional.get().getName());

        optional = personalAccountFolderRepository.findByNameAndAccountEntityId("name1", 2L);

        Assertions.assertTrue(optional.isEmpty());
    }

    @Test
    void findAllByAccountEntityIdShouldReturnOnlyRequiredEntities() {
        personalAccountFolderRepository.saveAndFlush(
                prePopulatedValidFolderBuilder().accountEntityId(1L).build()
        );
        personalAccountFolderRepository.saveAndFlush(
                prePopulatedValidFolderBuilder().accountEntityId(1L).build()
        );
        personalAccountFolderRepository.saveAndFlush(
                prePopulatedValidFolderBuilder().accountEntityId(2L).build()
        );

        List<PersonalAccountFolder> result = personalAccountFolderRepository.findAllByAccountEntityId(1L);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertTrue(result.stream().allMatch(folder -> Objects.equals(1L, folder.getAccountEntityId())));
    }

    @Test
    void findByUuidAndAccountEntityIdShouldReturnOnlyRequiredEntities() {
        personalAccountFolderRepository.saveAndFlush(
                prePopulatedValidFolderBuilder().accountEntityId(1L).uuid(UUID_1).build()
        );

        personalAccountFolderRepository.saveAndFlush(
                prePopulatedValidFolderBuilder().accountEntityId(1L).uuid(UUID_2).build()
        );

        Optional<PersonalAccountFolder> optional = personalAccountFolderRepository.findByUuidAndAccountEntityId(UUID_1, 1L);

        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(1L, optional.get().getAccountEntityId());
        Assertions.assertEquals(UUID_1, optional.get().getUuid());

        optional = personalAccountFolderRepository.findByUuidAndAccountEntityId(UUID_2, 1L);

        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(1L, optional.get().getAccountEntityId());
        Assertions.assertEquals(UUID_2, optional.get().getUuid());

        optional = personalAccountFolderRepository.findByUuidAndAccountEntityId(UUID_1, 2L);

        Assertions.assertTrue(optional.isEmpty());
    }

    @Test
    void existsByUuidAndAccountEntityId_shouldReturnTrueIfExists() {
        personalAccountFolderRepository.saveAndFlush(
                prePopulatedValidFolderBuilder().accountEntityId(1L).uuid(UUID_1).build()
        );

        boolean result = personalAccountFolderRepository.existsByUuidAndAccountEntityId(UUID_1, 1L);

        Assertions.assertTrue(result);
    }

    @Test
    void existsByUuidAndAccountEntityId_shouldReturnFalseIfNotExists() {
        personalAccountFolderRepository.saveAndFlush(
                prePopulatedValidFolderBuilder().accountEntityId(2L).uuid(UUID_1).build()
        );

        boolean result = personalAccountFolderRepository.existsByUuidAndAccountEntityId(UUID_1, 1L);

        Assertions.assertFalse(result);

        result = personalAccountFolderRepository.existsByUuidAndAccountEntityId(UUID_2, 1L);

        Assertions.assertFalse(result);

        result = personalAccountFolderRepository.existsByUuidAndAccountEntityId(UUID_2, 2L);

        Assertions.assertFalse(result);
    }
}
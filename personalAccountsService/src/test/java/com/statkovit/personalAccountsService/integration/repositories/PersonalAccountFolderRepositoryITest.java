package com.statkovit.personalAccountsService.integration.repositories;

import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.repository.PersonalAccountFolderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountFolderDomainHelper.prePopulatedValidFolderBuilder;


class PersonalAccountFolderRepositoryITest extends BaseRepositoryTest {

    @Autowired
    private PersonalAccountFolderRepository personalAccountFolderRepository;

    @Test
    void save_nameCanNotBeNull() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () ->
                personalAccountFolderRepository.saveAndFlush(
                        prePopulatedValidFolderBuilder().name(null).build()
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
}
package com.statkovit.personalAccountsService.repository;

import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonalAccountFolderRepository extends JpaRepository<PersonalAccountFolder, Long> {

    Optional<PersonalAccountFolder> findByNameAndAccountEntityId(String name, Long accountEntityId);

    Optional<PersonalAccountFolder> findByUuidAndAccountEntityId(UUID uuid, Long accountEntityId);

    boolean existsByUuidAndAccountEntityId(UUID uuid, Long accountEntityId);

    List<PersonalAccountFolder> findAllByAccountEntityId(Long accountEntityId);

    void deleteAllByAccountEntityId(Long accountEntityId);
}

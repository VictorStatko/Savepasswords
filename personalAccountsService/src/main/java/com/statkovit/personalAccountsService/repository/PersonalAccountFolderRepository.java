package com.statkovit.personalAccountsService.repository;

import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonalAccountFolderRepository extends JpaRepository<PersonalAccountFolder, Long> {

    Optional<PersonalAccountFolder> findByNameAndAccountEntityId(String name, Long accountEntityId);

    List<PersonalAccountFolder> findAllByAccountEntityId(Long accountEntityId);
}

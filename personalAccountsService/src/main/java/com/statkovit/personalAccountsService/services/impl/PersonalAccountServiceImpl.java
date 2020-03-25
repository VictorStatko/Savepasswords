package com.statkovit.personalAccountsService.services.impl;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.mappers.PersonalAccountMapper;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.repository.PersonalAccountRepository;
import com.statkovit.personalAccountsService.services.PersonalAccountService;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonalAccountServiceImpl implements PersonalAccountService {

    private final PersonalAccountMapper personalAccountMapper;
    private final PersonalAccountRepository personalAccountRepository;

    @Transactional
    @Override
    public PersonalAccount create(PersonalAccountDto personalAccountDto) {
        PersonalAccount account = personalAccountMapper.toEntity(personalAccountDto);

        account.setAccountEntityId(SecurityUtils.getCurrentAccountEntityId());

        return personalAccountRepository.save(account);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PersonalAccount> getList() {
        Long accountEntityId = SecurityUtils.getCurrentAccountEntityId();

        return personalAccountRepository.findAllByAccountEntityId(accountEntityId);
    }

    @Transactional
    @Override
    public void delete(UUID accountUuid) {
        final PersonalAccount personalAccount = findOneByUuid(accountUuid);
        personalAccountRepository.delete(personalAccount);
    }

    private PersonalAccount findOneByUuid(UUID accountUuid) {
        Long accountEntityId = SecurityUtils.getCurrentAccountEntityId();

        return personalAccountRepository.findByUuidAndAccountEntityId(accountUuid, accountEntityId).orElseThrow(
                () -> new LocalizedException(
                        new EntityNotFoundException("Personal account with uuid = " + accountUuid + " has not been found."),
                        "exceptions.personalAccountNotFoundByUuid"
                )
        );
    }
}

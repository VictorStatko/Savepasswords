package com.statkovit.personalAccountsService.services.impl;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.mappers.PersonalAccountMapper;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.repository.PersonalAccountRepository;
import com.statkovit.personalAccountsService.services.PersonalAccountService;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}

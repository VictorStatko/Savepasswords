package com.statkovit.personalAccountsService.rest.impl;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.mappers.PersonalAccountMapper;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.rest.PersonalAccountRestService;
import com.statkovit.personalAccountsService.services.PersonalAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonalAccountRestServiceImpl implements PersonalAccountRestService {

    private final PersonalAccountService personalAccountService;
    private final PersonalAccountMapper personalAccountMapper;

    @Override
    public PersonalAccountDto create(PersonalAccountDto personalAccountDto) {
        PersonalAccount accountForSave = personalAccountMapper.toEntity(personalAccountDto, new PersonalAccount());

        accountForSave = personalAccountService.save(accountForSave);

        return personalAccountMapper.toDto(accountForSave);
    }

    @Override
    public PersonalAccountDto update(UUID accountUuid, PersonalAccountDto personalAccountDto) {
        PersonalAccount accountForUpdate = personalAccountService.findOneByUuid(accountUuid);
        PersonalAccount accountAfterUpdate = personalAccountMapper.toEntity(personalAccountDto, accountForUpdate);

        accountAfterUpdate = personalAccountService.save(accountAfterUpdate);
        return personalAccountMapper.toDto(accountAfterUpdate);
    }

    @Override
    public List<PersonalAccountDto> getList() {
        List<PersonalAccount> accounts = personalAccountService.getList();

        return accounts.stream()
                .map(personalAccountMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID accountUuid) {
        personalAccountService.delete(accountUuid);
    }
}

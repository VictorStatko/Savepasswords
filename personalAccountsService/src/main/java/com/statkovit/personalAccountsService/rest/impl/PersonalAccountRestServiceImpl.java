package com.statkovit.personalAccountsService.rest.impl;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.mappers.PersonalAccountMapper;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.rest.PersonalAccountRestService;
import com.statkovit.personalAccountsService.services.PersonalAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonalAccountRestServiceImpl implements PersonalAccountRestService {

    private final PersonalAccountService personalAccountService;
    private final PersonalAccountMapper personalAccountMapper;

    @Override
    public PersonalAccountDto create(PersonalAccountDto personalAccountDto) {
        PersonalAccount account = personalAccountService.create(personalAccountDto);

        return personalAccountMapper.toDto(account);
    }

    @Override
    public List<PersonalAccountDto> getList() {
        List<PersonalAccount> accounts = personalAccountService.getList();

        return accounts.stream()
                .map(personalAccountMapper::toDto)
                .collect(Collectors.toList());
    }
}

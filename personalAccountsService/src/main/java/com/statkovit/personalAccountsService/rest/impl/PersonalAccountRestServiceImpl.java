package com.statkovit.personalAccountsService.rest.impl;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.payload.converters.PersonalAccountConverter;
import com.statkovit.personalAccountsService.payload.filters.PersonalAccountListFilters;
import com.statkovit.personalAccountsService.repository.expressions.PersonalAccountsExpressionsBuilder;
import com.statkovit.personalAccountsService.rest.PersonalAccountRestService;
import com.statkovit.personalAccountsService.services.PersonalAccountService;
import com.statkovit.personalAccountsService.validation.PersonalAccountFolderValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonalAccountRestServiceImpl implements PersonalAccountRestService {

    private final PersonalAccountService personalAccountService;
    private final PersonalAccountConverter personalAccountConverter;
    private final PersonalAccountsExpressionsBuilder expressionsBuilder;
    private final PersonalAccountFolderValidator folderValidator;

    @Override
    public PersonalAccountDto create(PersonalAccountDto personalAccountDto) {
        PersonalAccount accountForSave = new PersonalAccount();
        personalAccountConverter.toEntity(personalAccountDto, accountForSave);

        accountForSave = personalAccountService.save(accountForSave);

        return personalAccountConverter.toDto(accountForSave);
    }

    @Override
    public PersonalAccountDto update(UUID accountUuid, PersonalAccountDto personalAccountDto) {
        PersonalAccount accountForUpdate = personalAccountService.findOneByUuid(accountUuid);
        personalAccountConverter.toEntity(personalAccountDto, accountForUpdate);

        accountForUpdate = personalAccountService.save(accountForUpdate);
        return personalAccountConverter.toDto(accountForUpdate);
    }

    @Override
    public List<PersonalAccountDto> getList(PersonalAccountListFilters filters) {

        if (!filters.isUnfolderedOnly() && Objects.nonNull(filters.getFolderUuid())) {
            folderValidator.validateFolderExistence(filters.getFolderUuid());
        }

        List<PersonalAccount> accounts = personalAccountService.getList(
                expressionsBuilder.getListExpression(filters)
        );

        return accounts.stream()
                .map(personalAccountConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID accountUuid) {
        personalAccountService.delete(accountUuid);
    }
}

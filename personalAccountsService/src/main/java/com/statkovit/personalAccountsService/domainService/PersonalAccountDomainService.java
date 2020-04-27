package com.statkovit.personalAccountsService.domainService;

import com.statkovit.personalAccountsService.domain.AccountData;
import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.payload.LongDto;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.payload.PersonalAccountsSharingDto;
import com.statkovit.personalAccountsService.payload.converters.PersonalAccountConverter;
import com.statkovit.personalAccountsService.payload.filters.PersonalAccountListFilters;
import com.statkovit.personalAccountsService.services.AccountDataService;
import com.statkovit.personalAccountsService.services.PersonalAccountService;
import com.statkovit.personalAccountsService.validation.PersonalAccountFolderValidator;
import com.statkovit.personalAccountsService.validation.PersonalAccountValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonalAccountDomainService {

    private final PersonalAccountService personalAccountService;
    private final PersonalAccountConverter personalAccountConverter;
    private final PersonalAccountFolderValidator folderValidator;
    private final PersonalAccountValidator personalAccountValidator;
    private final AccountDataService accountDataService;

    @Transactional
    public PersonalAccountDto create(PersonalAccountDto personalAccountDto) {
        PersonalAccount accountForSave = new PersonalAccount();
        personalAccountConverter.toEntity(personalAccountDto, accountForSave);

        accountForSave = personalAccountService.save(accountForSave);

        return personalAccountConverter.toDto(accountForSave);
    }

    @Transactional
    public PersonalAccountDto update(UUID accountUuid, PersonalAccountDto personalAccountDto) {
        PersonalAccount accountForUpdate = personalAccountService.findOneByUuid(accountUuid);

        personalAccountValidator.validateUpdate(accountForUpdate, personalAccountDto);

        personalAccountConverter.toEntity(personalAccountDto, accountForUpdate);

        accountForUpdate = personalAccountService.save(accountForUpdate);
        return personalAccountConverter.toDto(accountForUpdate);
    }

    public List<PersonalAccountDto> getList(PersonalAccountListFilters filters) {
        if (!filters.isUnfolderedOnly() && Objects.nonNull(filters.getFolderUuid())) {
            folderValidator.validateFolderExistence(filters.getFolderUuid());
        }

        List<PersonalAccount> accounts = personalAccountService.getList(filters);

        return accounts.stream()
                .map(personalAccountConverter::toDto)
                .collect(Collectors.toList());
    }

    public LongDto getListCount(PersonalAccountListFilters filters) {
        if (!filters.isUnfolderedOnly() && Objects.nonNull(filters.getFolderUuid())) {
            folderValidator.validateFolderExistence(filters.getFolderUuid());
        }

        long count = personalAccountService.count(filters);

        return new LongDto(count);
    }

    @Transactional
    public void delete(UUID accountUuid) {
        final PersonalAccount personalAccount = personalAccountService.findOneByUuid(accountUuid);

        personalAccountService.delete(personalAccount);
    }

    @Transactional
    public PersonalAccountDto createSharedAccount(
            PersonalAccountDto dto,
            UUID sharedFromPersonalAccountUuid,
            UUID sharedToAccountDataUuid
    ) {
        final PersonalAccount sharedFromPersonalAccount = personalAccountService.findOneByUuid(sharedFromPersonalAccountUuid);
        final AccountData sharedToAccountData = accountDataService.internalGetByUuid(sharedToAccountDataUuid);

        personalAccountValidator.validateSharing(sharedFromPersonalAccount, sharedToAccountData);

        PersonalAccount accountForSave = new PersonalAccount();
        accountForSave.setAccountEntityId(sharedToAccountData.getId());
        accountForSave.setDuplicatedAccountEntity(sharedToAccountData);
        accountForSave.setParentPersonalAccount(sharedFromPersonalAccount);

        personalAccountConverter.toEntity(dto, accountForSave);

        accountForSave = personalAccountService.save(accountForSave);

        return personalAccountConverter.toDto(accountForSave);
    }

    public List<PersonalAccountsSharingDto> getSharing() {
        List<Pair<AccountData, Long>> sharing = personalAccountService.getSharing();
        return sharing.stream()
                .map(pair -> {
                    PersonalAccountsSharingDto dto = new PersonalAccountsSharingDto();
                    dto.setSharedAccountsCount(pair.getValue());
                    dto.setSharingFromAccountEntityUuid(pair.getLeft().getUuid());
                    dto.setSharingFromEmail(pair.getLeft().getEmail());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}

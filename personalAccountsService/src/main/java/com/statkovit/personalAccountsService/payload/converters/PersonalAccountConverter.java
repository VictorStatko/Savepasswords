package com.statkovit.personalAccountsService.payload.converters;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.encryptors.PersonalAccountsEncryptor;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.services.AccountDataService;
import com.statkovit.personalAccountsService.services.PersonalAccountFolderService;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public final class PersonalAccountConverter {
    private final PersonalAccountsEncryptor personalAccountsEncryptor;
    private final SecurityUtils securityUtils;
    private final PersonalAccountFolderService folderService;
    private final AccountDataService accountDataService;

    public void toEntity(PersonalAccountDto personalAccountDto, PersonalAccount personalAccount) {
        personalAccount.setUrl(personalAccountDto.getUrl());
        personalAccount.setName(personalAccountDto.getName());
        personalAccount.setUsername(personalAccountDto.getUsername());
        personalAccount.setPassword(personalAccountDto.getPassword());
        personalAccount.setDescription(personalAccountDto.getDescription());

        personalAccountsEncryptor.encryptFields(personalAccount);

        if (Objects.isNull(personalAccount.getAccountEntityId())) {
            personalAccount.setDuplicatedAccountEntity(accountDataService.internalGetById(securityUtils.getCurrentAccountEntityId()));
            personalAccount.setAccountEntityId(personalAccount.getDuplicatedAccountEntity().getId());
        }

        personalAccountDto.getSharedAccounts().forEach(sharedAccountDto ->
                personalAccount.getSharedAccounts().stream()
                        .filter(sharedAccount -> Objects.equals(sharedAccountDto.getUuid(), sharedAccount.getUuid()))
                        .findFirst()
                        .ifPresent(sharedAccount -> toEntity(sharedAccountDto, sharedAccount))
        );

        if (Objects.isNull(personalAccountDto.getFolderUuid())) {
            personalAccount.setFolder(null);
        } else {
            personalAccount.setFolder(folderService.getByUuid(personalAccountDto.getFolderUuid()));
        }
    }

    public PersonalAccountDto toDto(PersonalAccount account) {
        PersonalAccountDto dto = new PersonalAccountDto();

        dto.setUuid(account.getUuid());
        dto.setDescription(account.getDescription());
        dto.setFolderUuid(Objects.nonNull(account.getFolder()) ? account.getFolder().getUuid() : null);
        dto.setName(account.getName());
        dto.setUsername(account.getUsername());
        dto.setPassword(account.getPassword());
        dto.setUrl(account.getUrl());
        dto.setEncryptionPublicKey(account.getDuplicatedAccountEntity().getPublicKey());
        dto.setOwnerEmail(account.getDuplicatedAccountEntity().getEmail());
        dto.setSharedAccounts(
                account.getSharedAccounts().stream().map(this::toDto).collect(Collectors.toList())
        );

        personalAccountsEncryptor.decryptFields(account.getFieldsEncryptionSalt(), dto);

        return dto;
    }
}

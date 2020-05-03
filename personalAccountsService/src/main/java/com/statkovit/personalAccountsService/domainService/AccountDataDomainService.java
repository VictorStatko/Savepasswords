package com.statkovit.personalAccountsService.domainService;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.domain.AccountData;
import com.statkovit.personalAccountsService.payload.AccountDataDto;
import com.statkovit.personalAccountsService.payload.AccountDataForSharingDto;
import com.statkovit.personalAccountsService.payload.converters.AccountDataConverter;
import com.statkovit.personalAccountsService.services.AccountDataService;
import com.statkovit.personalAccountsService.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountDataDomainService {
    private final AccountDataConverter dataConverter;
    private final AccountDataService accountDataService;
    private final SecurityUtils securityUtils;

    @Transactional
    public void create(AccountDataDto dto) {
        AccountData data = new AccountData();

        dataConverter.toEntity(dto, data);

        accountDataService.save(data);
    }

    @Transactional
    public void remove(AccountDataDto dto) {
        AccountData data = accountDataService.internalGetById(dto.getId());

        accountDataService.fullRemove(data);
    }

    public AccountDataForSharingDto getAccountDataForSharing(String accountEmail) {
        AccountData accountData = accountDataService.internalGetByEmail(accountEmail);

        if (accountData.getId().equals(securityUtils.getCurrentAccountEntityId())) {
            throw new LocalizedException(
                    "Self sharing is not allowed",
                    "exceptions.selfSharingNotAllowed"
            );
        }

        AccountDataForSharingDto dto = new AccountDataForSharingDto();

        dto.setEmail(accountData.getEmail());
        dto.setEntityUuid(accountData.getUuid());
        dto.setPublicKey(accountData.getPublicKey());

        return dto;
    }
}

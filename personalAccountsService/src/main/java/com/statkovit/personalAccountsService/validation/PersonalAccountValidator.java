package com.statkovit.personalAccountsService.validation;

import com.statkolibraries.exceptions.exceptions.LocalizedException;
import com.statkovit.personalAccountsService.domain.AccountData;
import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PersonalAccountValidator {

    public void validateUpdate(PersonalAccount accountBeforeUpdate, PersonalAccountDto dto) {
        if (accountBeforeUpdate.getParentPersonalAccount() != null) {
            throw new LocalizedException(
                    String.format("Account with uuid %s is a shared account and can be updated only after parent update.", accountBeforeUpdate.getUuid()),
                    "exceptions.sharedAccountCannotBeUpdated"
            );
        }


        final int sharedAccountSizeBeforeUpdate = accountBeforeUpdate.getSharedAccounts().size();
        final int sharedAccountSizeAfterUpdate = dto.getSharedAccounts().size();

        if (sharedAccountSizeBeforeUpdate != sharedAccountSizeAfterUpdate) {
            throw new LocalizedException(
                    String.format("Not expected sharedAccounts size. Expected %s, actual %s.", sharedAccountSizeBeforeUpdate, sharedAccountSizeAfterUpdate),
                    "exceptions.notExpectedSizeSharedAccounts"
            );
        }

        accountBeforeUpdate.getSharedAccounts().forEach(sharedAccount -> {
            boolean isUpdatedAccountExists = dto.getSharedAccounts().stream()
                    .anyMatch(sharedAccountDto -> Objects.equals(sharedAccount.getUuid(), sharedAccountDto.getUuid()));

            if (!isUpdatedAccountExists) {
                throw new LocalizedException(
                        String.format("Updated data for shared account with uuid %s is not provided.", sharedAccount.getUuid()),
                        "exceptions.sharedAccountUpdateRequired"
                );
            }
        });
    }

    public void validateSharing(PersonalAccount originalAccount, AccountData sharedToAccountData) {
        if (originalAccount.getParentPersonalAccount() != null) {
            throw new LocalizedException(
                    String.format("Account with uuid %s is a shared account and can't be shared.", originalAccount.getUuid()),
                    "exceptions.sharedAccountCannotBeShared"
            );
        }

        if (originalAccount.getDuplicatedAccountEntity().equals(sharedToAccountData)) {
            throw new LocalizedException(
                    "Self sharing is not allowed",
                    "exceptions.selfSharingNotAllowed"
            );
        }

        final boolean alreadyShared = originalAccount.getSharedAccounts().stream()
                .anyMatch(sharedAccount -> sharedAccount.getDuplicatedAccountEntity().equals(sharedToAccountData));

        if (alreadyShared) {
            throw new LocalizedException(
                    String.format("Personal account with uuid %s is a already shared to user account with uuid %s.", originalAccount.getUuid(), sharedToAccountData.getUuid()),
                    "exceptions.accountAlreadyShared"
            );
        }
    }
}

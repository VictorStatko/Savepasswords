package unit.helper.domain;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;

import java.util.UUID;

public final class PersonalAccountDomainHelper {

    public static PersonalAccount account() {
        return new PersonalAccount();
    }

    public static PersonalAccount account(UUID uuid, Long entityId) {
        PersonalAccount accountMock = new PersonalAccount();
        accountMock.setUuid(uuid);
        accountMock.setAccountEntityId(entityId);
        return accountMock;
    }

    public static PersonalAccount account(UUID uuid) {
        PersonalAccount accountMock = new PersonalAccount();
        accountMock.setUuid(uuid);
        return accountMock;
    }

    public static PersonalAccount account(Long entityId) {
        PersonalAccount accountMock = new PersonalAccount();
        accountMock.setAccountEntityId(entityId);
        return accountMock;
    }

    public static PersonalAccountDto accountDTO(UUID uuid) {
        PersonalAccountDto dtoMock = accountDTO();
        dtoMock.setUuid(uuid);
        return dtoMock;
    }

    public static PersonalAccountDto accountDTO() {
        return new PersonalAccountDto();
    }
}

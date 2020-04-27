package com.statkovit.personalAccountsService.controllers.external;

import com.statkovit.personalAccountsService.domainService.AccountDataDomainService;
import com.statkovit.personalAccountsService.domainService.PersonalAccountDomainService;
import com.statkovit.personalAccountsService.payload.AccountDataForSharingDto;
import com.statkovit.personalAccountsService.payload.LongDto;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto.CreationValidation;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto.SharingValidation;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto.UpdateValidation;
import com.statkovit.personalAccountsService.payload.PersonalAccountsSharingDto;
import com.statkovit.personalAccountsService.payload.filters.PersonalAccountListFilters;
import com.statkovit.personalAccountsService.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

import static com.statkovit.personalAccountsService.constants.MappingConstants.PersonalAccountsExternalController.*;

@RestController
@RequiredArgsConstructor
@Validated
public class PersonalAccountsController {

    private final PersonalAccountDomainService personalAccountDomainService;
    private final AccountDataDomainService accountDataDomainService;

    @PostMapping(CREATE_ROUTE)
    public ResponseEntity<PersonalAccountDto> createPersonalAccount(
            @Validated({CreationValidation.class}) @RequestBody PersonalAccountDto dto
    ) {
        dto = personalAccountDomainService.create(dto);

        return ResponseEntity.ok(dto);
    }

    @RequestMapping(
            value = CREATE_ROUTE,
            method = RequestMethod.POST,
            params = "type=shared"
    )
    public ResponseEntity<PersonalAccountDto> sharePersonalAccount(
            @Validated({SharingValidation.class}) @RequestBody PersonalAccountDto dto,
            @RequestParam @NotNull UUID fromPersonalAccountUuid, @RequestParam @NotNull UUID toUserAccountUuid
    ) {
        dto = personalAccountDomainService.createSharedAccount(dto, fromPersonalAccountUuid, toUserAccountUuid);

        return ResponseEntity.ok(dto);
    }

    @GetMapping(GET_SHARING_LIST_ROUTE)
    public ResponseEntity<List<PersonalAccountsSharingDto>> getSharingList() {
        List<PersonalAccountsSharingDto> sharing = personalAccountDomainService.getSharing();
        return ResponseEntity.ok(sharing);
    }

    @GetMapping(GET_ACCOUNT_DATA_FOR_SHARING_ROUTE)
    public ResponseEntity<AccountDataForSharingDto> getAccountDataForSharing(@RequestParam @NotEmpty @Email String accountEmail) {
        AccountDataForSharingDto dto = accountDataDomainService.getAccountDataForSharing(accountEmail);
        return ResponseEntity.ok(dto);
    }

    @PutMapping(UPDATE_ROUTE)
    public ResponseEntity<PersonalAccountDto> updatePersonalAccount(@PathVariable UUID uuid, @Validated({UpdateValidation.class}) @RequestBody PersonalAccountDto dto) {
        dto = personalAccountDomainService.update(uuid, dto);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(DELETE_ROUTE)
    public ResponseEntity<?> deletePersonalAccount(@PathVariable UUID uuid) {
        personalAccountDomainService.delete(uuid);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(GET_LIST_ROUTE)
    public ResponseEntity<List<PersonalAccountDto>> getPersonalAccounts(PersonalAccountListFilters filters) {
        List<PersonalAccountDto> accountDtos = personalAccountDomainService.getList(filters);

        return ResponseEntity.ok(accountDtos);
    }

    @GetMapping(GET_LIST_COUNT_ROUTE)
    public ResponseEntity<LongDto> getPersonalAccountsCount(PersonalAccountListFilters filters) {
        LongDto count = personalAccountDomainService.getListCount(filters);

        return ResponseEntity.ok(count);
    }

}

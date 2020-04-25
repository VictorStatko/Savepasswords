package com.statkovit.personalAccountsService.controllers.external;

import com.statkovit.personalAccountsService.payload.LongDto;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.payload.filters.PersonalAccountListFilters;
import com.statkovit.personalAccountsService.dataService.PersonalAccountDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.statkovit.personalAccountsService.constants.MappingConstants.PersonalAccountsExternalController.*;

@RestController
@RequiredArgsConstructor
public class PersonalAccountsController {

    private final PersonalAccountDataService personalAccountDataService;

    @PostMapping(CREATE_ROUTE)
    public ResponseEntity<PersonalAccountDto> createPersonalAccount(@Valid @RequestBody PersonalAccountDto dto) {
        dto = personalAccountDataService.create(dto);

        return ResponseEntity.ok(dto);
    }

    @PutMapping(UPDATE_ROUTE)
    public ResponseEntity<PersonalAccountDto> updatePersonalAccount(@PathVariable UUID uuid, @Valid @RequestBody PersonalAccountDto dto) {
        dto = personalAccountDataService.update(uuid, dto);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(DELETE_ROUTE)
    public ResponseEntity<?> deletePersonalAccount(@PathVariable UUID uuid) {
        personalAccountDataService.delete(uuid);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(GET_LIST_ROUTE)
    public ResponseEntity<List<PersonalAccountDto>> getPersonalAccounts(PersonalAccountListFilters filters) {
        List<PersonalAccountDto> accountDtos = personalAccountDataService.getList(filters);

        return ResponseEntity.ok(accountDtos);
    }

    @GetMapping(GET_LIST_COUNT_ROUTE)
    public ResponseEntity<LongDto> getPersonalAccountsCount(PersonalAccountListFilters filters) {
        LongDto count = personalAccountDataService.getListCount(filters);

        return ResponseEntity.ok(count);
    }

}

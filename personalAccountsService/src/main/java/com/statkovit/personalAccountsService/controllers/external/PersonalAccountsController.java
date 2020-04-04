package com.statkovit.personalAccountsService.controllers.external;

import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.rest.PersonalAccountRestService;
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

    private final PersonalAccountRestService personalAccountRestService;

    @PostMapping(CREATE_ROUTE)
    public ResponseEntity<PersonalAccountDto> createPersonalAccount(@Valid @RequestBody PersonalAccountDto dto) {
        dto = personalAccountRestService.create(dto);

        return ResponseEntity.ok(dto);
    }

    @PutMapping(UPDATE_ROUTE)
    public ResponseEntity<PersonalAccountDto> updatePersonalAccount(@PathVariable UUID uuid, @Valid @RequestBody PersonalAccountDto dto) {
        dto = personalAccountRestService.update(uuid, dto);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(DELETE_ROUTE)
    public ResponseEntity<PersonalAccountDto> deletePersonalAccount(@PathVariable UUID uuid) {
        personalAccountRestService.delete(uuid);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(GET_LIST_ROUTE)
    public ResponseEntity<List<PersonalAccountDto>> getPersonalAccounts() {
        List<PersonalAccountDto> accountDtos = personalAccountRestService.getList();

        return ResponseEntity.ok(accountDtos);
    }


}

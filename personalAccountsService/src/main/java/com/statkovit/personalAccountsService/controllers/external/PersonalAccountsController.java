package com.statkovit.personalAccountsService.controllers.external;

import com.statkovit.personalAccountsService.constants.ServerConstants;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.rest.PersonalAccountRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PersonalAccountsController {

    private static final String CONTROLLER_ROUTE = ServerConstants.EXTERNAL_API_ROUTE + "accounts";

    private final PersonalAccountRestService personalAccountRestService;

    @PostMapping(CONTROLLER_ROUTE)
    public ResponseEntity<PersonalAccountDto> createPersonalAccount(@Valid @RequestBody PersonalAccountDto dto) {
        dto = personalAccountRestService.create(dto);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(CONTROLLER_ROUTE + "/{uuid}")
    public ResponseEntity<PersonalAccountDto> deletePersonalAccount(@PathVariable UUID uuid) {
        personalAccountRestService.delete(uuid);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(CONTROLLER_ROUTE)
    public ResponseEntity<List<PersonalAccountDto>> getPersonalAccounts() {
        List<PersonalAccountDto> accountDtos = personalAccountRestService.getList();

        return ResponseEntity.ok(accountDtos);
    }


}

package com.statkovit.personalAccountsService.controllers.external;

import com.statkovit.personalAccountsService.payload.PersonalAccountFolderDto;
import com.statkovit.personalAccountsService.rest.PersonalAccountFolderRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.statkovit.personalAccountsService.constants.MappingConstants.FoldersExternalController.CREATE_ROUTE;

@RestController
@RequiredArgsConstructor
public class FoldersController {

    private final PersonalAccountFolderRestService folderRestService;

    @PostMapping(CREATE_ROUTE)
    public ResponseEntity<PersonalAccountFolderDto> createFolder(@Valid @RequestBody PersonalAccountFolderDto dto) {
        dto = folderRestService.create(dto);

        return ResponseEntity.ok(dto);
    }
}

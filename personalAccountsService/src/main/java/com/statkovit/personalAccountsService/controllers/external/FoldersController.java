package com.statkovit.personalAccountsService.controllers.external;

import com.statkovit.personalAccountsService.domainService.PersonalAccountFolderDomainService;
import com.statkovit.personalAccountsService.enums.FolderRemovalOptions;
import com.statkovit.personalAccountsService.payload.PersonalAccountFolderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.statkovit.personalAccountsService.constants.MappingConstants.FoldersExternalController.*;

@RestController
@RequiredArgsConstructor
public class FoldersController {

    private final PersonalAccountFolderDomainService folderDomainService;

    @PostMapping(CREATE_ROUTE)
    public ResponseEntity<PersonalAccountFolderDto> createFolder(@Valid @RequestBody PersonalAccountFolderDto dto) {
        dto = folderDomainService.create(dto);

        return ResponseEntity.ok(dto);
    }

    @PutMapping(UPDATE_ROUTE)
    public ResponseEntity<PersonalAccountFolderDto> updateFolder(@PathVariable UUID uuid, @Valid @RequestBody PersonalAccountFolderDto dto) {
        dto = folderDomainService.update(uuid, dto);

        return ResponseEntity.ok(dto);
    }

    @GetMapping(GET_LIST_ROUTE)
    public ResponseEntity<List<PersonalAccountFolderDto>> getFolderListOfCurrentAccountEntity() {
        List<PersonalAccountFolderDto> folderDtos = folderDomainService.getListOfCurrentAccountEntity();

        return ResponseEntity.ok(folderDtos);
    }

    @DeleteMapping(DELETE_ROUTE)
    public ResponseEntity<?> deleteFolder(@PathVariable UUID uuid, @RequestParam("removalOption") FolderRemovalOptions removalOptions) {
        folderDomainService.delete(uuid, removalOptions);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

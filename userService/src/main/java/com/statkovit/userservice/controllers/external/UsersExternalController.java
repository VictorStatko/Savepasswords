package com.statkovit.userservice.controllers.external;

import com.statkovit.userservice.constants.ServerConstants;
import com.statkovit.userservice.dto.UserDto;
import com.statkovit.userservice.rest.UserRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UsersExternalController {

    private static final String CONTROLLER_ENDPOINT = ServerConstants.EXTERNAL_API_V1_ENDPOINT + "users";

    private final UserRestService userRestService;

    @PostMapping(CONTROLLER_ENDPOINT)
    public ResponseEntity<UserDto> createNewUser(@Valid @RequestBody UserDto userDto) {
        UserDto result = userRestService.createNewUser(userDto);
        return ResponseEntity.ok(result);
    }
}

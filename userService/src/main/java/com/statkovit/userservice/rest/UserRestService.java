package com.statkovit.userservice.rest;

import com.statkovit.userservice.dto.UserDto;

public interface UserRestService {
    UserDto createNewUser(UserDto dto);
}

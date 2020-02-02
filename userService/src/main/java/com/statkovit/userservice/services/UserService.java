package com.statkovit.userservice.services;

import com.statkovit.userservice.domain.User;
import com.statkovit.userservice.dto.UserDto;

public interface UserService {
    User create(UserDto userDto);
}

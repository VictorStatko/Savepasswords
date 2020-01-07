package com.statkovit.userservice.services;

import com.statkovit.userservice.domain.User;
import com.statkovit.userservice.dto.UserDto;
import com.statkovit.userservice.feign.payload.AccountDto;
import org.apache.commons.lang3.tuple.Pair;

public interface UserService {
    Pair<User, AccountDto> create(UserDto userDto);
}

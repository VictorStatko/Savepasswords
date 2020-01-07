package com.statkovit.userservice.rest.impl;

import com.statkovit.userservice.domain.User;
import com.statkovit.userservice.dto.UserDto;
import com.statkovit.userservice.feign.payload.AccountDto;
import com.statkovit.userservice.rest.UserRestService;
import com.statkovit.userservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRestServiceImpl implements UserRestService {
    private final UserService userService;

    @Override
    public UserDto createNewUser(UserDto dto) {
        final Pair<User, AccountDto> data = userService.create(dto);
        final User user = data.getLeft();
        final AccountDto account = data.getRight();

        UserDto result = new UserDto();
        result.setEmail(account.getEmail());
        result.setId(user.getId());
        result.setName(user.getName());
        return result;
    }
}

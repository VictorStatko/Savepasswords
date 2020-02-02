package com.statkovit.userservice.rest.impl;

import com.statkovit.userservice.domain.User;
import com.statkovit.userservice.dto.UserDto;
import com.statkovit.userservice.mappers.UserMapper;
import com.statkovit.userservice.rest.UserRestService;
import com.statkovit.userservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRestServiceImpl implements UserRestService {
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public UserDto createNewUser(UserDto dto) {
        final User user = userService.create(dto);

        return userMapper.toDto(user);
    }
}

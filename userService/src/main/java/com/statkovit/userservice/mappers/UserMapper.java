package com.statkovit.userservice.mappers;

import com.statkovit.userservice.domain.User;
import com.statkovit.userservice.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(user, UserDto.class);
    }
}

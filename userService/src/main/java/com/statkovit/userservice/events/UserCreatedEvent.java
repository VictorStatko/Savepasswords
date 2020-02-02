package com.statkovit.userservice.events;

import com.statkovit.userservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class UserCreatedEvent {
    private UserDto userDto;

    private UUID userUuid;
}

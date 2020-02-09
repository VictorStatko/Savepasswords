package com.statkovit.userservice.dto;

import com.statkovit.userservice.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
public class UserDto {
    public static final int MAX_LENGTH__PASSWORD = 60;
    public static final int MIN_LENGTH__PASSWORD = 8;

    private UUID uuid;

    @NotEmpty
    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = MIN_LENGTH__PASSWORD, max = MAX_LENGTH__PASSWORD)
    private String password;
}

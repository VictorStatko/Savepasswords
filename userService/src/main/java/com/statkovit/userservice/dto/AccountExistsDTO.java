package com.statkovit.userservice.dto;

import com.statkovit.userservice.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountExistsDTO {
    @Email
    private String email;
}

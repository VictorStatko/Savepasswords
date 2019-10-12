package com.statkovit.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDataDTO {

    private Long id;

    private UUID uuid;

    List<String> roles;

    List<String> permissions;
}

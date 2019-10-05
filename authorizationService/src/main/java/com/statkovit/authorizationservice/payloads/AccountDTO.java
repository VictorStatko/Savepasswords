package com.statkovit.authorizationservice.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private Long id;

    private UUID uuid;

    List<String> roles;

    List<String> permissions;

}

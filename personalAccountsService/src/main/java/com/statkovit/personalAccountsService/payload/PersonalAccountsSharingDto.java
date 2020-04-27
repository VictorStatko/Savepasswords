package com.statkovit.personalAccountsService.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class PersonalAccountsSharingDto {

    private String sharingFromEmail;
    private UUID sharingFromAccountEntityUuid;
    private Long sharedAccountsCount;
}

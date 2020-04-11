package com.statkovit.personalAccountsService.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuperBuilder
@NoArgsConstructor
public class PersonalAccountFolderDto {

    @EqualsAndHashCode.Include
    private UUID uuid;

    @NotEmpty
    @Size(max = PersonalAccountFolder.MAX_LENGTH__NAME)
    private String name;

    private Integer accountsCount;
}

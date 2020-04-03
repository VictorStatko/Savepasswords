package com.statkovit.personalAccountsService.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.ScriptAssert;

import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuperBuilder
@NoArgsConstructor
@ScriptAssert(lang = "javascript", script = "_this.url != null || _this.name != null")
public class PersonalAccountDto {

    @EqualsAndHashCode.Include
    private UUID uuid;

    private String url;

    private String name;

    private String username;

    private String password;

    private String description;
}

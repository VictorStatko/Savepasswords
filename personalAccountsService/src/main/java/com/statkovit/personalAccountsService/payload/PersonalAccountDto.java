package com.statkovit.personalAccountsService.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ScriptAssert(lang = "javascript", script = "_this.url != null || _this.name != null")
public class PersonalAccountDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uuid;

    private String url;

    private String name;

    private String username;

    private String password;

    private String description;
}

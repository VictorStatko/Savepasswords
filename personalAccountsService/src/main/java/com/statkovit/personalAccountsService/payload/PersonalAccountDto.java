package com.statkovit.personalAccountsService.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.statkovit.personalAccountsService.domain.PersonalAccount;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ScriptAssert(lang = "javascript", script = "_this.url != null || _this.name != null")
public class PersonalAccountDto {

    private UUID uuid;

    @Size(max = PersonalAccount.MAX_LENGTH__URL)
    private String url;

    @Size(max = PersonalAccount.MAX_LENGTH__NAME)
    private String name;

    private String username;

    private String password;

    private String description;
}

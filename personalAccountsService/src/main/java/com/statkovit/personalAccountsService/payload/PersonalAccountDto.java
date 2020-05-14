package com.statkovit.personalAccountsService.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@ScriptAssert(lang = "javascript", script = "_this.url != null || _this.name != null")
public class PersonalAccountDto {

    @EqualsAndHashCode.Include
    @Null(groups = {CreationValidation.class, SharingValidation.class})
    @NotNull(groups = {UpdateValidation.class, NestedUpdateValidation.class})
    private UUID uuid;

    private String url;

    private String name;

    private String username;

    private String password;

    private String description;

    private String encryptionPublicKey;

    @NotEmpty
    private String encryptedAesClientKey;

    private String ownerEmail;

    @Null(groups = {SharingValidation.class, NestedUpdateValidation.class})
    private UUID folderUuid;

    @Size(max = 0, groups = {CreationValidation.class, SharingValidation.class, NestedUpdateValidation.class})
    private List<@Valid @ConvertGroup(from = UpdateValidation.class, to = NestedUpdateValidation.class) PersonalAccountDto> sharedAccounts = new ArrayList<>();

    public interface CreationValidation extends Default {
    }

    public interface SharingValidation extends Default {
    }

    public interface UpdateValidation extends Default {
    }

    public interface NestedUpdateValidation extends Default {
    }

    public List<PersonalAccountDto> getSharedAccounts() {
        return sharedAccounts == null ? new ArrayList<>() : sharedAccounts;
    }
}

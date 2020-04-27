package com.statkovit.personalAccountsService.payload.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ScriptAssert(lang = "javascript", script = "_this.folderUuid == null || _this.parentPersonalAccountAccountEntityUuid == null")
public class PersonalAccountListFilters {

    private boolean unfolderedOnly = false;

    private UUID folderUuid;

    private UUID parentPersonalAccountAccountEntityUuid;
}

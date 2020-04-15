package com.statkovit.personalAccountsService.payload.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalAccountListFilters {

    private boolean unfolderedOnly = false;

    private String folderUuid;
}

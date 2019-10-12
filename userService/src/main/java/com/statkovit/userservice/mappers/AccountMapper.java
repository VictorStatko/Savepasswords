package com.statkovit.userservice.mappers;

import com.statkovit.userservice.domain.Account;
import com.statkovit.userservice.dto.AccountDataDTO;
import com.statkovit.userservice.dto.CredentialsDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    private final ModelMapper modelMapper;

    public Account toEntity(CredentialsDTO credentialsDTO) {
        return modelMapper.map(credentialsDTO, Account.class);
    }

    public AccountDataDTO toAccountDataDto(Account account) {
        AccountDataDTO accountDataDTO = modelMapper.map(account, AccountDataDTO.class);
        //TODO replace to real
        accountDataDTO.setPermissions(Collections.singletonList("first permission"));
        accountDataDTO.setRoles(Collections.singletonList("first role"));
        return accountDataDTO;
    }

}

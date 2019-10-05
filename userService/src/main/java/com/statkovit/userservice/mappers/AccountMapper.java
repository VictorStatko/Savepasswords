package com.statkovit.userservice.mappers;

import com.statkovit.userservice.domain.Account;
import com.statkovit.userservice.dto.AccountLoginDTO;
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

    public AccountLoginDTO toAccountLoginDto(Account account) {
        AccountLoginDTO accountLoginDTO = modelMapper.map(account, AccountLoginDTO.class);
        //TODO replace to real
        accountLoginDTO.setPermissions(Collections.singletonList("first permission"));
        accountLoginDTO.setRoles(Collections.singletonList("first role"));
        return accountLoginDTO;
    }

}

package com.statkovit.authorizationservice.mappers;

import com.statkovit.authorizationservice.domain.Account;
import com.statkovit.authorizationservice.payload.AccountDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    public AccountDto toDto(Account account) {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<AccountDto, Account>() {
            @Override
            protected void configure() {
                skip(destination.getPassword());
            }
        });

        return modelMapper.map(account, AccountDto.class);
    }
}

package com.statkovit.authorizationservice.mappers;

import com.statkovit.authorizationservice.domain.Account;
import com.statkovit.authorizationservice.payload.AccountDto;
import com.statkovit.authorizationservice.payload.ExtendedAccountDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    public AccountDto toDto(Account account) {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<Account, AccountDto>() {
            @Override
            protected void configure() {
                skip(destination.getPassword());
                skip(destination.getPrivateKey());
                skip(destination.getPublicKey());
                skip(destination.getClientPasswordSalt());
            }
        });

        return modelMapper.map(account, AccountDto.class);
    }

    public ExtendedAccountDto toExtendedDto(Account account) {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<Account, ExtendedAccountDto>() {
            @Override
            protected void configure() {
                skip(destination.getPassword());
                skip(destination.getPrivateKey());
                skip(destination.getPublicKey());
                skip(destination.getClientPasswordSalt());
            }
        });

        return modelMapper.map(account, ExtendedAccountDto.class);
    }
}

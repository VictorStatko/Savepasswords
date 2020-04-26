package com.statkovit.authorizationservice.mappers;

import com.statkovit.authorizationservice.entities.Account;
import com.statkovit.authorizationservice.payload.kafka.AccountKafkaDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountKafkaMapper {

    public AccountKafkaDto toDto(Account account) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(account, AccountKafkaDto.class);
    }

}

package com.statkovit.authorizationservice.kafka.mappers;

import com.statkovit.authorizationservice.domain.Account;
import com.statkovit.authorizationservice.kafka.dto.AccountKafkaDto;
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

package com.statkovit.userservice.mappers;

import com.statkovit.userservice.domain.Account;
import com.statkovit.userservice.dto.SignUpDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    private final ModelMapper modelMapper;

    public Account toEntity(SignUpDTO signUpDTO) {
        return modelMapper.map(signUpDTO, Account.class);
    }

}

package com.statkovit.personalAccountsService.payload.mappers;

import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class PersonalAccountMapper {
    public void toEntity(PersonalAccountDto personalAccountDto, PersonalAccount personalAccount) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<PersonalAccountDto, PersonalAccount>() {
            @Override
            protected void configure() {
                skip(destination.getUuid());
            }
        });

        modelMapper.map(personalAccountDto, personalAccount);
    }

    public PersonalAccountDto toDto(PersonalAccount personalAccount) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(personalAccount, PersonalAccountDto.class);
    }
}

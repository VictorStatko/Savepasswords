package com.statkovit.personalAccountsService.payload.mappers;

import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.payload.PersonalAccountFolderDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonalAccountFolderMapper {

    public void toEntity(PersonalAccountFolderDto folderDto, PersonalAccountFolder folder) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<PersonalAccountFolderDto, PersonalAccountFolder>() {
            @Override
            protected void configure() {
                skip(destination.getUuid());
            }
        });

        modelMapper.map(folderDto, folder);
    }

    public PersonalAccountFolderDto toDto(PersonalAccountFolder personalAccountFolder) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(personalAccountFolder, PersonalAccountFolderDto.class);
    }


}

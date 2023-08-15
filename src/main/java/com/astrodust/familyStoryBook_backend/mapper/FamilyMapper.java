package com.astrodust.familyStoryBook_backend.mapper;

import com.astrodust.familyStoryBook_backend.dto.FamilyRegisterDTO;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FamilyMapper {

    private final ModelMapper modelMapper;

    public FamilyMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public FamilyAccount toEntity(FamilyRegisterDTO familyRegisterDTO){
        FamilyAccount account = modelMapper.map(familyRegisterDTO, FamilyAccount.class);
        account.setCreatedDate(LocalDateTime.now());
        return account;
    }
}

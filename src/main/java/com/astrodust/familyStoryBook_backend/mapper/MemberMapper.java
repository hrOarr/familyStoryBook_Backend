package com.astrodust.familyStoryBook_backend.mapper;

import com.astrodust.familyStoryBook_backend.dto.MemberDTO;
import com.astrodust.familyStoryBook_backend.dto.MemberInfoGeneralDTO;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.model.MemberAccount;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MemberMapper {

    private final ModelMapper modelMapper;

    public MemberMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public MemberDTO toDto(MemberAccount account) {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        MemberDTO memberDTO = modelMapper.map(account, MemberDTO.class);
        memberDTO.setName(memberDTO.getFirstName()+" "+memberDTO.getLastName());
        memberDTO.setFamily_id(account.getFamilyAccount().getId());
        return memberDTO;
    }

    public MemberAccount memberInfoGeneralToEntity(MemberInfoGeneralDTO memberInfoGeneralDTO, FamilyAccount familyAccount, MemberAccount parent) {
        MemberAccount account = modelMapper.map(memberInfoGeneralDTO, MemberAccount.class);
        account.setFamilyAccount(familyAccount);

        // if root or have parent
        if(parent!=null) {
            account.setParent(parent);
            if(account.getId()<=0) parent.getChildren().add(account);
        }

        if(account.getId()<=0) { // insertion
            account.setCreatedDate(LocalDateTime.now());
        } else { // updation
            account.setUpdatedDate(LocalDateTime.now());
        }
        familyAccount.getMemberAccounts().add(account);
        return account;
    }

    public MemberInfoGeneralDTO entityToMemberInfoGeneralDTO(MemberAccount memberAccount) {
        return modelMapper.map(memberAccount, MemberInfoGeneralDTO.class);
    }
}

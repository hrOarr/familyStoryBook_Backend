package com.astrodust.familyStoryBook_backend.mapper;

import com.astrodust.familyStoryBook_backend.dto.MemberInsertEducationDTO;
import com.astrodust.familyStoryBook_backend.dto.MemberUpdateEducationDTO;
import com.astrodust.familyStoryBook_backend.model.MemberAccount;
import com.astrodust.familyStoryBook_backend.model.MemberEducation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EducationMapper {

    private final ModelMapper modelMapper;

    public EducationMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public List<MemberEducation> memberInsertEducationDTOtoEntity(List<MemberInsertEducationDTO> memberInsertEducationDTO, MemberAccount memberAccount){
        List<MemberEducation> educations = new ArrayList<MemberEducation>();

        for(MemberInsertEducationDTO insertEducationDTO:memberInsertEducationDTO) {
            MemberEducation memberEducation = new MemberEducation();
            memberEducation.setInstitution(insertEducationDTO.getInstitution());
            memberEducation.setDescription(insertEducationDTO.getDescription());
            memberEducation.setStartDate(insertEducationDTO.getStartDate());
            memberEducation.setEndDate(insertEducationDTO.getEndDate());
            memberEducation.setMemberAccount(memberAccount);
            educations.add(memberEducation);
        }

        return educations;
    }

    public MemberEducation memberUpdateEducationDTOtoEntity(MemberUpdateEducationDTO memberUpdateEducationDTO, MemberAccount memberAccount){
        MemberEducation memberEducation = modelMapper.map(memberUpdateEducationDTO, MemberEducation.class);
        memberEducation.setMemberAccount(memberAccount);
        return memberEducation;
    }
}

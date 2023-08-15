package com.astrodust.familyStoryBook_backend.mapper;

import com.astrodust.familyStoryBook_backend.dto.MemberInsertJobDTO;
import com.astrodust.familyStoryBook_backend.dto.MemberUpdateJobDTO;
import com.astrodust.familyStoryBook_backend.model.MemberAccount;
import com.astrodust.familyStoryBook_backend.model.MemberJob;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JobMapper {

    private final ModelMapper modelMapper;

    public JobMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public List<MemberJob> memberInsertJobDTOtoEntity(List<MemberInsertJobDTO> memberInsertJobDTO, MemberAccount memberAccount){
        List<MemberJob> memberJobs = new ArrayList<>();

        for(MemberInsertJobDTO insertJobDTO:memberInsertJobDTO){
            MemberJob memberJob = new MemberJob();
            memberJob.setCompanyName(insertJobDTO.getCompanyName());
            memberJob.setJobRole(insertJobDTO.getJobRole());
            memberJob.setDescription(insertJobDTO.getDescription());
            memberJob.setLocation(insertJobDTO.getLocation());
            memberJob.setJoinDate(insertJobDTO.getJoinDate());
            memberJob.setEndDate(insertJobDTO.getEndDate());
            memberJob.setMemberAccount(memberAccount);
            memberJobs.add(memberJob);
        }
        return memberJobs;
    }

    public MemberJob memberUpdateJobDTOtoEntity(MemberUpdateJobDTO memberUpdateJobDTO, MemberAccount memberAccount){
        MemberJob memberJob = modelMapper.map(memberUpdateJobDTO, MemberJob.class);
        memberJob.setMemberAccount(memberAccount);
        return memberJob;
    }
}

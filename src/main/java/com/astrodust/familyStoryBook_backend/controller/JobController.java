package com.astrodust.familyStoryBook_backend.controller;

import com.astrodust.familyStoryBook_backend.dto.MemberInsertJobDTO;
import com.astrodust.familyStoryBook_backend.dto.MemberUpdateJobDTO;
import com.astrodust.familyStoryBook_backend.exception.AccessDeniedException;
import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import com.astrodust.familyStoryBook_backend.mapper.JobMapper;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.model.MemberJob;
import com.astrodust.familyStoryBook_backend.service.interfaces.FamilyService;
import com.astrodust.familyStoryBook_backend.service.interfaces.JobService;
import com.astrodust.familyStoryBook_backend.service.interfaces.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/members/{memberId}/jobs")
public class JobController {

    private final JobService jobService;
    private final MemberService memberService;
    private final FamilyService familyService;
    private final JobMapper jobMapper;

    @Autowired
    public JobController(JobService jobService, MemberService memberService, FamilyService familyService, JobMapper jobMapper){
        this.jobService = jobService;
        this.memberService = memberService;
        this.familyService = familyService;
        this.jobMapper = jobMapper;
    }

    @ApiOperation(value = "Get Job-list by MemberId")
    @GetMapping(value = "/")
    public ResponseEntity<?> getByMemberId(@PathVariable(name = "memberId") int memberId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getByMemberId(memberId));
    }

    @ApiOperation(value = "Save Job-list")
    @PostMapping(value = "/")
    public ResponseEntity<?> save(@Valid @RequestBody List<MemberInsertJobDTO> memberInsertJobDTO,
                                  @PathVariable(name = "memberId") int memberId) throws Exception {
        List<MemberJob> memberJobs = jobService.save(jobMapper.memberInsertJobDTOtoEntity(memberInsertJobDTO, memberService.getById(memberId)));
        return ResponseEntity.status(HttpStatus.CREATED).body(memberJobs);
    }

    @ApiOperation(value = "Get Job By Id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") int id,
                                  @PathVariable(name = "memberId") int memberId) {
        MemberJob memberJob = jobService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(memberJob);
    }

    @ApiOperation(value = "Update Job")
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody MemberUpdateJobDTO memberUpdateJobDTO,
                                    @PathVariable(name = "id") int id,
                                    @PathVariable(name = "memberId") int memberId) throws Exception {
        memberUpdateJobDTO.setId(id);
        MemberJob memberJob = jobService.update(jobMapper.memberUpdateJobDTOtoEntity(memberUpdateJobDTO, memberService.getById(memberId)));
        return ResponseEntity.status(HttpStatus.OK).body(memberJob);
    }

    @ApiOperation(value = "Delete Job By Id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete( @PathVariable(name = "id") int id,
                                     @PathVariable(name = "memberId") int memberId){
        int count = jobService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(count+ " row(s) deleted");
    }

    public void IsAuthorized(int fid,int mid,int jid){
        String currentUsername = "";
        FamilyAccount familyAccount = familyService.getById(fid);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            currentUsername = (((UserDetails) principal).getUsername());
        }
        else{
            currentUsername = principal.toString();
        }
        if(familyAccount==null || currentUsername.equals(familyAccount.getEmail())==false) {
            throw new AccessDeniedException("You are not authorized to access");
        }

        if(jid!=0){
            MemberJob memberJob = jobService.getById(jid);
            if(memberJob == null) {
                throw new ResourceNotFoundException("Resource Not Found");
            }
            if(memberJob.getMemberAccount().getId()!=mid){
                throw new AccessDeniedException("You are not authorized to access");
            }
        }
    }
}

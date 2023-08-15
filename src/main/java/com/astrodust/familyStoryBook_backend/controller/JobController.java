package com.astrodust.familyStoryBook_backend.controller;

import com.astrodust.familyStoryBook_backend.dto.MemberInsertJobDTO;
import com.astrodust.familyStoryBook_backend.dto.MemberUpdateJobDTO;
import com.astrodust.familyStoryBook_backend.exception.AccessDeniedException;
import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import com.astrodust.familyStoryBook_backend.helpers.Converter;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.model.MemberJob;
import com.astrodust.familyStoryBook_backend.service.interfaces.FamilyService;
import com.astrodust.familyStoryBook_backend.service.interfaces.JobService;
import com.astrodust.familyStoryBook_backend.service.interfaces.MemberService;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/member/job")
public class JobController {

    private static final Logger logger = LogManager.getLogger(JobController.class);
    private JobService jobService;
    private MemberService memberService;
    private FamilyService familyService;
    private Converter converter;

    @Autowired
    public JobController(JobService jobService, MemberService memberService, FamilyService familyService, Converter converter){
        this.jobService = jobService;
        this.memberService = memberService;
        this.familyService = familyService;
        this.converter = converter;
    }

    @ApiOperation(value = "Get Job-list by MemberId")
    @GetMapping(value = "/getAll/memberId/{memberId}/familyId/{familyId}")
    public ResponseEntity<?> getByMemberId(@PathVariable(name = "memberId") int mid, @PathVariable(name = "familyId") int fid) throws Exception {
        try {
            // authorization check
            IsAuthorized(fid, mid, 0);
            return ResponseEntity.status(HttpStatus.OK).body(jobService.getByMemberId(mid));
        }
        catch (AccessDeniedException e){
            throw new AccessDeniedException(e.getLocalizedMessage());
        }
        catch (Exception e){
            logger.info("SoA:: exception from getByMemberId() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Save Job-list")
    @PostMapping(value = "/save/memberId/{memberId}/familyId/{familyId}")
    public ResponseEntity<?> save(@Valid @RequestBody List<MemberInsertJobDTO> memberInsertJobDTO,
                                  @PathVariable(name = "memberId") int mid, @PathVariable(name = "familyId") int fid) throws Exception {
        try {
            // authorization check
            IsAuthorized(fid, mid, 0);
            List<MemberJob> memberJobs = jobService.save(converter.memberInsertJobDTOtoMemberJob(memberInsertJobDTO, memberService.getById(mid)));
            return ResponseEntity.status(HttpStatus.CREATED).body(memberJobs);
        }
        catch (AccessDeniedException e){
            throw new AccessDeniedException(e.getLocalizedMessage());
        }
        catch (Exception e){
            logger.info("SoA:: exception from save() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Get Job for update")
    @GetMapping(value = "/edit/id/{id}/memberId/{memberId}/familyId/{familyId}")
    public ResponseEntity<?> edit(@PathVariable(name = "id") int jid, @PathVariable(name = "memberId") int mid, @PathVariable(name = "familyId") int fid) throws Exception {
        try {
            // authorization check
            IsAuthorized(fid, mid, jid);
            MemberJob memberJob = jobService.getById(jid);
            return ResponseEntity.status(HttpStatus.OK).body(memberJob);
        }
        catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(e.getLocalizedMessage());
        }
        catch (AccessDeniedException e){
            throw new AccessDeniedException(e.getLocalizedMessage());
        }
        catch (Exception e){
            logger.info("SoA:: exception from edit() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Update Job")
    @PutMapping(value = "/update/id/{id}/memberId/{memberId}/familyId/{familyId}")
    public ResponseEntity<?> update(@Valid @RequestBody MemberUpdateJobDTO memberUpdateJobDTO,
                                    @PathVariable(name = "id") int jid,
                                    @PathVariable(name = "memberId") int mid, @PathVariable(name = "familyId") int fid) throws Exception {
        try {
            // authorization check
            IsAuthorized(fid, mid, jid);
            memberUpdateJobDTO.setId(jid);
            MemberJob memberJob = jobService.update(converter.MemberUpdateJobDTOtoMemberJob(memberUpdateJobDTO, memberService.getById(mid)));
            return ResponseEntity.status(HttpStatus.OK).body(memberJob);
        }
        catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(e.getLocalizedMessage());
        }
        catch (AccessDeniedException e){
            throw new AccessDeniedException(e.getLocalizedMessage());
        }
        catch (Exception e){
            logger.info("SoA:: exception from update() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Delete A Job")
    @DeleteMapping(value = "/delete/id/{id}/memberId/{memberId}/familyId/{familyId}")
    public ResponseEntity<?> delete( @PathVariable(name = "id") int jid,
                                     @PathVariable(name = "memberId") int mid, @PathVariable(name = "familyId") int fid){
        // authorization check
        IsAuthorized(fid, mid, jid);
        int cnt = jobService.delete(jid);
        logger.info(cnt + " rows deleted");
        return ResponseEntity.status(HttpStatus.OK).body(cnt+ " row(s) deleted");
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
        logger.info("SoA :: " + currentUsername);
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

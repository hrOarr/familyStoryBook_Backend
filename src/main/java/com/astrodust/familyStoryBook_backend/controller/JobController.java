package com.astrodust.familyStoryBook_backend.controller;

import com.astrodust.familyStoryBook_backend.dto.MemberInsertJobDTO;
import com.astrodust.familyStoryBook_backend.dto.MemberUpdateJobDTO;
import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import com.astrodust.familyStoryBook_backend.helpers.Converter;
import com.astrodust.familyStoryBook_backend.model.MemberAccount;
import com.astrodust.familyStoryBook_backend.model.MemberJob;
import com.astrodust.familyStoryBook_backend.service.JobService;
import com.astrodust.familyStoryBook_backend.service.MemberService;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/member/job")
public class JobController {

    private static final Logger logger = LogManager.getLogger(JobController.class);
    private JobService jobService;
    private MemberService memberService;
    private Converter converter;

    @Autowired
    public JobController(JobService jobService, MemberService memberService, Converter converter){
        this.jobService = jobService;
        this.memberService = memberService;
        this.converter = converter;
    }

    @ApiOperation(value = "Get Job-list by MemberId")
    @GetMapping(value = "/getByMemberId/{memberId}")
    public ResponseEntity<?> getByMemberId(@PathVariable(name = "memberId") int memberId) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(jobService.getByMemberId(memberId));
        }
        catch (Exception e){
            logger.info("SoA:: exception from getByMemberId() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Save Job-list")
    @PostMapping(value = "/save/{memberId}")
    public ResponseEntity<?> save(@Valid @RequestBody MemberInsertJobDTO memberInsertJobDTO,
                                  @PathVariable(name = "memberId") int memberId) throws Exception {
        try {
            logger.info("JobController save() method init->>>>>>>>");
            MemberAccount memberAccount = memberService.getById(memberId);
            if (memberAccount == null) {
                throw new ResourceNotFoundException("Resource Not Found");
            }
            List<MemberJob> memberJobs = jobService.save(converter.memberInsertJobDTOtoMemberJob(memberInsertJobDTO, memberAccount));
            return ResponseEntity.status(HttpStatus.CREATED).body(memberJobs);
        }
        catch (Exception e){
            logger.info("SoA:: exception from save() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Get Job for update")
    @GetMapping(value = "/edit/{id}")
    public ResponseEntity<?> edit(@PathVariable int id) throws Exception {
        try {
            MemberJob memberJob = jobService.getById(id);
            if (memberJob == null) {
                throw new ResourceNotFoundException("Resource Not Found!");
            }
            return ResponseEntity.status(HttpStatus.OK).body(memberJob);
        }
        catch (Exception e){
            logger.info("SoA:: exception from edit() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Update Job")
    @PutMapping(value = "/update/{memberId}")
    public ResponseEntity<?> update(@Valid @RequestBody MemberUpdateJobDTO memberUpdateJobDTO,
                                    @PathVariable(name = "memberId") int memberId) throws Exception {
        try {
            MemberAccount memberAccount = memberService.getById(memberId);
            if (memberAccount == null) {
                throw new ResourceNotFoundException("Member Account is required!");
            }
            MemberJob memberJob = jobService.update(converter.MemberUpdateJobDTOtoMemberJob(memberUpdateJobDTO, memberAccount));
            return ResponseEntity.status(HttpStatus.OK).body(memberJob);
        }
        catch (Exception e){
            logger.info("SoA:: exception from update() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Delete A Job")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        int cnt = jobService.delete(id);
        logger.info(cnt + " rows deleted");
        return ResponseEntity.status(HttpStatus.OK).body(cnt+ " row(s) deleted");
    }
}

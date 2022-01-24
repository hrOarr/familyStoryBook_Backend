package com.astrodust.familyStoryBook_backend.controller;

import com.astrodust.familyStoryBook_backend.dto.AchievementDTO;
import com.astrodust.familyStoryBook_backend.dto.InsertAchievementDTO;
import com.astrodust.familyStoryBook_backend.dto.UpdateAchievementDTO;
import com.astrodust.familyStoryBook_backend.exception.AccessDeniedException;
import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import com.astrodust.familyStoryBook_backend.helpers.Converter;
import com.astrodust.familyStoryBook_backend.model.Achievement;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.model.MemberAccount;
import com.astrodust.familyStoryBook_backend.service.AchievementService;
import com.astrodust.familyStoryBook_backend.service.FamilyService;
import com.astrodust.familyStoryBook_backend.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/member/achievement")
public class AchievementController {
    private static final Logger logger = LogManager.getLogger(AchievementController.class);
    private ModelMapper modelMapper;
    private Converter converter;
    private AchievementService achievementService;
    private MemberService memberService;
    private FamilyService familyService;

    @Autowired
    public AchievementController(Converter converter, ModelMapper modelMapper, AchievementService achievementService, MemberService memberService, FamilyService familyService){
        this.converter = converter;
        this.modelMapper = modelMapper;
        this.achievementService = achievementService;
        this.memberService = memberService;
        this.familyService = familyService;
    }

    @ApiOperation(value = "Save Achievement List")
    @PostMapping(value = "/save/memberId/{memberId}/familyId/{familyId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> save(@RequestParam("data") String str, @RequestPart(name = "image", required = false) MultipartFile image,
                                  @PathVariable(name = "memberId") int mid, @PathVariable(name = "familyId") int fid) throws Exception {
        try {
            // authorization check
            IsAuthorized(fid, mid, 0);
            InsertAchievementDTO insertAchievementDTO = new ObjectMapper().readValue(str, InsertAchievementDTO.class);
            insertAchievementDTO.setImage(image);
            logger.info(insertAchievementDTO);
            List<Achievement> achievements = achievementService.save(converter.InsertAchievementDTOtoAchievement(insertAchievementDTO, memberService.getById(mid)));
            return ResponseEntity.status(HttpStatus.CREATED).body(achievements);
        }
        catch (AccessDeniedException e){
            throw new AccessDeniedException(e.getLocalizedMessage());
        }
        catch (Exception e){
            logger.info("SoA:: exception from save() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Get All by Member-Id")
    @GetMapping(value = "/getAll/memberId/{memberId}/familyId/{familyId}")
    public ResponseEntity<?> getByMemberId(@PathVariable(name = "memberId") int mid, @PathVariable(name = "familyId") int fid) throws Exception {
        try {
            // authorization check
            IsAuthorized(fid, mid, 0);
            List<Achievement> achievements = achievementService.getAllByMemberId(mid);
            List<AchievementDTO> achievementDTOS = new ArrayList<>();
            for (Achievement achievement : achievements) {
                achievementDTOS.add(converter.achievementToAchievementDTO(achievement));
            }
            return ResponseEntity.status(HttpStatus.OK).body(achievementDTOS);
        }
        catch (AccessDeniedException e){
            throw new AccessDeniedException(e.getLocalizedMessage());
        }
        catch (Exception e){
            logger.info("SoA:: exception from getByMemberId() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Get Single One")
    @GetMapping(value = "/edit/id/{id}/memberId/{memberId}/familyId/{familyId}")
    public ResponseEntity<?> edit(@PathVariable(name = "id") int id, @PathVariable(name = "memberId") int mid,
                                  @PathVariable(name = "familyId") int fid) throws Exception {

        try {
            // authorization check
            IsAuthorized(fid, mid, id);
            Achievement achievement = achievementService.getById(id);
            return ResponseEntity.status(HttpStatus.OK).body(converter.achievementToAchievementDTO(achievement));
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

    @ApiOperation(value = "Update Single One")
    @PostMapping(value = "/update/id/{id}/memberId/{memberId}/familyId/{familyId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> update(@RequestParam("data") String str, @RequestPart(name = "image", required = false) MultipartFile image,
                                    @PathVariable(name = "id") int id,@PathVariable(name = "memberId") int mid, @PathVariable(name = "familyId") int fid) throws Exception {
        try {
            // authorization check
            IsAuthorized(fid, mid, id);
            UpdateAchievementDTO updateAchievementDTO = new ObjectMapper().readValue(str, UpdateAchievementDTO.class);
            updateAchievementDTO.setId(id);
            updateAchievementDTO.setImage(image);
            MemberAccount memberAccount = memberService.getById(mid);
            Achievement achievement = achievementService.update(converter.updateAchievementDTOtoAchievement(updateAchievementDTO, memberAccount));
            return ResponseEntity.status(HttpStatus.OK).body(achievement);
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

    @ApiOperation(value = "Delete Single One")
    @DeleteMapping(value = "/delete/id/{id}/memberId/{memberId}/familyId/{familyId}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id,@PathVariable(name = "memberId") int mid, @PathVariable(name = "familyId") int fid) throws Exception {
        try {
            // authorization check
            IsAuthorized(fid, mid, id);
            int cnt = achievementService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(cnt + " row(s) deleted");
        }
        catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(e.getLocalizedMessage());
        }
        catch (AccessDeniedException e){
            throw new AccessDeniedException(e.getLocalizedMessage());
        }
        catch (Exception e){
            logger.info("SoA:: exception from delete() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    public void IsAuthorized(int fid,int mid,int aid){
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

        if(aid!=0){
            Achievement achievement = achievementService.getById(aid);
            if(achievement == null) {
                throw new ResourceNotFoundException("Resource Not Found");
            }
            if(achievement.getMemberAccount().getId()!=mid){
                throw new AccessDeniedException("You are not authorized to access");
            }
        }
    }
}

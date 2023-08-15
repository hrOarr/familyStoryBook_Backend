package com.astrodust.familyStoryBook_backend.controller;

import com.astrodust.familyStoryBook_backend.dto.AchievementDTO;
import com.astrodust.familyStoryBook_backend.dto.InsertAchievementDTO;
import com.astrodust.familyStoryBook_backend.dto.UpdateAchievementDTO;
import com.astrodust.familyStoryBook_backend.exception.AccessDeniedException;
import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import com.astrodust.familyStoryBook_backend.mapper.AchievementMapper;
import com.astrodust.familyStoryBook_backend.model.Achievement;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.service.interfaces.AchievementService;
import com.astrodust.familyStoryBook_backend.service.interfaces.FamilyService;
import com.astrodust.familyStoryBook_backend.service.interfaces.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/members/{memberId}/achievements")
public class AchievementController {
    private final AchievementService achievementService;
    private final MemberService memberService;
    private final FamilyService familyService;
    private final AchievementMapper achievementMapper;

    public AchievementController(AchievementService achievementService, MemberService memberService, FamilyService familyService, AchievementMapper achievementMapper){
        this.achievementService = achievementService;
        this.memberService = memberService;
        this.familyService = familyService;
        this.achievementMapper = achievementMapper;
    }

    @ApiOperation(value = "Save Achievement")
    @PostMapping(value = "/", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> save(@RequestParam("data") String str,
                                  @RequestPart(name = "image", required = false) MultipartFile image,
                                  @PathVariable(name = "memberId") int memberId) throws Exception {
        InsertAchievementDTO insertAchievementDTO = new ObjectMapper().readValue(str, InsertAchievementDTO.class);
        insertAchievementDTO.setImage(image);
        log.info("{}", insertAchievementDTO);
        List<AchievementDTO> achievementDTOS = achievementService.save(insertAchievementDTO, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(achievementDTOS);
    }

    @ApiOperation(value = "Get All by Member-Id")
    @GetMapping(value = "/")
    public ResponseEntity<?> findByMemberId(@PathVariable(name = "memberId") int memberId) {
        List<AchievementDTO> achievementDTOS = achievementService.getAllByMemberId(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(achievementDTOS);
    }

    @ApiOperation(value = "Get Achievement By Id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") int id,
                                      @PathVariable(name = "memberId") int memberId) {
        Achievement achievement = achievementService.getById(id);
        if(achievement==null){
            throw new ResourceNotFoundException("Resource not found with id = " + id);
        }
        return ResponseEntity.status(HttpStatus.OK).body(achievementMapper.toDto(achievement));
    }

    @ApiOperation(value = "Update Achievement")
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> update(@RequestParam("data") String str,
                                    @RequestPart(name = "image", required = false) MultipartFile image,
                                    @PathVariable(name = "id") int id,
                                    @PathVariable(name = "memberId") int memberId) throws Exception {
        UpdateAchievementDTO updateAchievementDTO = new ObjectMapper().readValue(str, UpdateAchievementDTO.class);
        updateAchievementDTO.setId(id);
        updateAchievementDTO.setImage(image);
        AchievementDTO achievementDTO = achievementService.update(updateAchievementDTO, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(achievementDTO);
    }

    @ApiOperation(value = "Delete Achievement")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id,
                                    @PathVariable(name = "memberId") int memberId) throws Exception {
        int count = achievementService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(count + " row(s) deleted");
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
        log.info("Username: {}", currentUsername);
        if(familyAccount==null || !currentUsername.equals(familyAccount.getEmail())) {
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

package com.astrodust.familyStoryBook_backend.controller;

import com.astrodust.familyStoryBook_backend.dto.AchievementDTO;
import com.astrodust.familyStoryBook_backend.dto.InsertAchievementDTO;
import com.astrodust.familyStoryBook_backend.dto.UpdateAchievementDTO;
import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import com.astrodust.familyStoryBook_backend.helpers.Converter;
import com.astrodust.familyStoryBook_backend.model.Achievement;
import com.astrodust.familyStoryBook_backend.model.MemberAccount;
import com.astrodust.familyStoryBook_backend.service.AchievementService;
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

    @Autowired
    public AchievementController(Converter converter, ModelMapper modelMapper, AchievementService achievementService, MemberService memberService){
        this.converter = converter;
        this.modelMapper = modelMapper;
        this.achievementService = achievementService;
        this.memberService = memberService;
    }

    @ApiOperation(value = "Save Achievement List")
    @PostMapping(value = "/save/{memberId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> save(@RequestParam("data") String str, @RequestPart(name = "image", required = false) MultipartFile image,
                                  @PathVariable(name = "memberId") int memberId) throws Exception {
        try {
            InsertAchievementDTO insertAchievementDTO = new ObjectMapper().readValue(str, InsertAchievementDTO.class);
            insertAchievementDTO.setImage(image);
            logger.info(insertAchievementDTO);
            List<Achievement> achievements = achievementService.save(converter.InsertAchievementDTOtoAchievement(insertAchievementDTO, memberService.getById(memberId)));
            return ResponseEntity.status(HttpStatus.CREATED).body(achievements);
        }
        catch (Exception e){
            logger.info("SoA:: exception from save() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Get All by Member-Id")
    @GetMapping(value = "/getByMember/{memberId}")
    public ResponseEntity<?> getByMemberId(@PathVariable(name = "memberId") int memberId) throws Exception {
        try {
            List<Achievement> achievements = achievementService.getAllByMemberId(memberId);
            List<AchievementDTO> achievementDTOS = new ArrayList<>();
            for (Achievement achievement : achievements) {
                achievementDTOS.add(converter.achievementToAchievementDTO(achievement));
            }
            return ResponseEntity.status(HttpStatus.OK).body(achievementDTOS);
        }
        catch (Exception e){
            logger.info("SoA:: exception from getByMemberId() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Get Single One")
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable int id){
        Achievement achievement = achievementService.getById(id);
        if(achievement==null){
            throw new ResourceNotFoundException("Resource Not Found with id = " + id);
        }
        return ResponseEntity.status(HttpStatus.OK).body(converter.achievementToAchievementDTO(achievement));
    }

    @ApiOperation(value = "Update Single One")
    @PostMapping(value = "/update/{memberId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> update(@RequestParam("data") String str, @RequestPart(name = "image", required = false) MultipartFile image,
                                    @PathVariable(name = "memberId") int memberId) throws Exception {
        try {
            UpdateAchievementDTO updateAchievementDTO = new ObjectMapper().readValue(str, UpdateAchievementDTO.class);
            updateAchievementDTO.setImage(image);
            MemberAccount memberAccount = memberService.getById(memberId);
            Achievement achievement = achievementService.update(converter.updateAchievementDTOtoAchievement(updateAchievementDTO, memberAccount));
            return ResponseEntity.status(HttpStatus.OK).body(achievement);
        }
        catch (Exception e){
            logger.info("SoA:: exception from update() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Delete Single One")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        int cnt = achievementService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(cnt + " row(s) deleted");
    }
}

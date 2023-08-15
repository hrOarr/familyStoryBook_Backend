package com.astrodust.familyStoryBook_backend.mapper;

import com.astrodust.familyStoryBook_backend.dto.AchievementDTO;
import com.astrodust.familyStoryBook_backend.dto.InsertAchievementDTO;
import com.astrodust.familyStoryBook_backend.dto.UpdateAchievementDTO;
import com.astrodust.familyStoryBook_backend.utils.ImageCompressionDecom;
import com.astrodust.familyStoryBook_backend.model.Achievement;
import com.astrodust.familyStoryBook_backend.model.ImageModel;
import com.astrodust.familyStoryBook_backend.model.MemberAccount;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class AchievementMapper {

    public AchievementDTO toDto(Achievement achievement){
        String str = Base64.getEncoder().encodeToString(ImageCompressionDecom.decompressBytes(achievement.getImage().getPicByte()));
        return AchievementDTO.builder().id(achievement.getId())
                .description(achievement.getDescription())
                .title(achievement.getTitle())
                .addedDate(achievement.getAddedDate())
                .updatedDate(achievement.getUpdatedDate())
                .imageModel(achievement.getImage())
                .imageBase64("data:"+achievement.getImage().getType()+";base64,"+ str)
                .imageId(achievement.getImage().getId())
                .imageName(achievement.getImage().getName())
                .imageType(achievement.getImage().getType())
                .build();
    }

    public List<AchievementDTO> toDto(List<Achievement> achievements){
        List<AchievementDTO> achievementDTOS = new ArrayList<>();
        achievements.stream().forEach(achievement -> achievementDTOS.add(toDto(achievement)));
        return achievementDTOS;
    }

    public List<Achievement> toEntity(InsertAchievementDTO insertAchievementDTO, MemberAccount memberAccount) throws IOException {
        List<Achievement> achievements = new ArrayList<>();
        ImageModel imageModel = new ImageModel();
        imageModel.setName(insertAchievementDTO.getImage().getOriginalFilename());
        imageModel.setType(insertAchievementDTO.getImage().getContentType());
        imageModel.setPicByte(ImageCompressionDecom.compressBytes(insertAchievementDTO.getImage().getBytes()));

        Achievement achievement = Achievement.builder().title(insertAchievementDTO.getTitle())
                .description(insertAchievementDTO.getDescription())
                .image(imageModel)
                .addedDate(LocalDateTime.now())
                .memberAccount(memberAccount)
                .build();
        achievements.add(achievement);
        return achievements;
    }

    public Achievement updateAchievementDTOtoEntity(UpdateAchievementDTO updateAchievementDTO, MemberAccount memberAccount) throws IOException {
        ImageModel imageModel = new ImageModel();
        imageModel.setId(updateAchievementDTO.getImage_id());
        imageModel.setName(updateAchievementDTO.getImage().getOriginalFilename());
        imageModel.setType(updateAchievementDTO.getImage().getContentType());
        imageModel.setPicByte(ImageCompressionDecom.compressBytes(updateAchievementDTO.getImage().getBytes()));

        return Achievement.builder().id(updateAchievementDTO.getId())
                .title(updateAchievementDTO.getTitle())
                .description(updateAchievementDTO.getDescription())
                .image(imageModel)
                .addedDate(updateAchievementDTO.getAddedDate())
                .updatedDate(LocalDateTime.now())
                .memberAccount(memberAccount)
                .build();
    }
}

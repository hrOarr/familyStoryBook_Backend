package com.astrodust.familyStoryBook_backend.dto;

import com.astrodust.familyStoryBook_backend.model.ImageModel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AchievementDTO {
    private int id;
    private String title;
    private String description;
    private LocalDateTime addedDate;
    private LocalDateTime updatedDate;
    private int imageId;
    private String imageName;
    private String imageType;
    private String imageBase64;
    private ImageModel imageModel;
}

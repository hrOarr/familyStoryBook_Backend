package com.astrodust.familyStoryBook_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AchievementDTO {
    private int id;
    private String title;
    private String description;
    private LocalDateTime addedDate;
    private LocalDateTime updatedDate;
    private int image_id;
    private String image_name;
    private String image_type;
    private String imageBase64;
}

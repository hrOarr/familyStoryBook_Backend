package com.astrodust.familyStoryBook_backend.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class UpdateAchievementDTO {
    private int id;
    private String title;
    private String description;
    private MultipartFile image;
    private int image_id;
    private LocalDateTime addedDate;
}

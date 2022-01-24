package com.astrodust.familyStoryBook_backend.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
public class UpdateAchievementDTO {

    @ApiModelProperty(value = "id")
    private int id;

    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty(message = "Description is required")
    private String description;
    private MultipartFile image;
    private int image_id;
    private LocalDate addedDate;
}

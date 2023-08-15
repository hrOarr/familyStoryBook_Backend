package com.astrodust.familyStoryBook_backend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel(value = "InsertAchievementDTO", description = "Achievement fields for insertion")
public class InsertAchievementDTO {

    @ApiModelProperty(value = "id")
    private int id;

    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty(message = "Description is required")
    private String description;

    private MultipartFile image;
}

package com.astrodust.familyStoryBook_backend.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FamilyLoginResponseDTO")
public class FamilyLoginResponseDTO {
    private int id;
    private String username;
    private String email;
    private String token;
}

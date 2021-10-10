package com.astrodust.familyStoryBook_backend.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FamilyLoginRequestDTO", description = "Family Login")
public class FamilyLoginRequestDTO {
	
	private int id;
	
	@Email(message = "Email must be valid")
	private String email;
	
	@Size(min = 8, max = 55, message = "Password must be between 8 and 55 characters")
	private String password;
}

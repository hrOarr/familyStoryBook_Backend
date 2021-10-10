package com.astrodust.familyStoryBook_backend.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FamilyRegisterDTO", description = "Family Registration model with needed fields")
public class FamilyRegisterDTO {
	
	@ApiModelProperty(value = "FamilyId")
	private int id;
	
	@NotEmpty(message = "Username can't be empty")
	@Size(min = 5, max = 55, message = "Username must be between 5 and 55 characters")
	private String username;
	
	@Email(message = "Email must be valid")
	private String email;
	
	@Size(min = 8, max = 55, message = "Password must be between 8 and 55 characters")
	private String password;
	
}

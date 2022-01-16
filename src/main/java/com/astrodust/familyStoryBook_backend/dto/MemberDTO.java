package com.astrodust.familyStoryBook_backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.astrodust.familyStoryBook_backend.model.MemberAccount;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "MemberDTO", description = "Member Minimum Fields")
public class MemberDTO {
	
	private int id;
	
	@NotEmpty(message = "FirstName can't be empty")
	@Size(min = 3, max = 55, message = "FirstName must be between 3 and 55 characters")
	private String firstName;
	
	@NotEmpty(message = "LastName can't be empty")
	@Size(min = 3, max = 55, message = "LastName must be between 3 and 55 characters")
	private String lastName;

	private String name;
	
	@Email(message = "Email must be valid")
	private String email;
	
	@NotEmpty(message = "Gender can't be empty")
	private String gender;
	
	private int parent_id;
	
	@Positive(message = "Invalid FamilyId")
	private int family_id;

	private LocalDate birthDate;
	private LocalDate deathDate;
	private String country;
	
	//private MemberAccount parent;
	private Set<MemberAccount> children;
	
	// member education
	
}

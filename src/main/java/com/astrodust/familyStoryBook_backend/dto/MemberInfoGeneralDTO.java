package com.astrodust.familyStoryBook_backend.dto;

import java.util.Calendar;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "MemberInitDTO", description = "Member Init Minimum Fields")
public class MemberInfoGeneralDTO {
	
	private int id;
	
	@NotEmpty(message = "FirstName can't be empty")
	@Size(min = 3, max = 55, message = "FirstName must be between 3 and 55 characters")
	private String firstName;
	
	@NotEmpty(message = "LastName can't be empty")
	@Size(min = 3, max = 55, message = "LastName must be between 3 and 55 characters")
	private String lastName;
	
	@Email(message = "Email must be valid")
	private String email;
	
	@NotEmpty(message = "Gender can't be empty")
	private String gender;
	
//	private int parent_id;
//	private int family_id;
	
	private Calendar birthDate;
	private Calendar deathDate;
	private String country;
	
	@Temporal(TemporalType.DATE)
	private Calendar createdDate;
	
	@Temporal(TemporalType.DATE)
	private Calendar updatedDate;
	
	// member education
	
}
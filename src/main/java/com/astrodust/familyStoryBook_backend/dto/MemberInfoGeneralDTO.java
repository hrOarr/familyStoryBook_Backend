package com.astrodust.familyStoryBook_backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
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

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate birthDate;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate deathDate;

	private String country;

	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	
	// member education
	
}
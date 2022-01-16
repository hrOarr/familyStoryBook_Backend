package com.astrodust.familyStoryBook_backend.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "member_education")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberEducation {
	
	// properties
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "education_id")
	private int id;
	
	@Column(name = "institution", nullable = false, length = 255)
	private String institution;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "startDate")
	private LocalDate startDate;
	
	@Column(name = "endDate")
	private LocalDate endDate;
	
	// relationship
	
	@ManyToOne
	@JoinColumn(name = "member_id")
	@JsonIgnore
	private MemberAccount memberAccount;
	
}

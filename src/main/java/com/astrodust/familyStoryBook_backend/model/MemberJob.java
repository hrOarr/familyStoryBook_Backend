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
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Temporal;

@Entity
@Table(name = "member_job")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJob {
	
	// properties
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "job_id")
	private int id;
	
	@Column(name = "companyName", nullable = false, length = 100)
	private String companyName;

	@Column(name = "location", nullable = false, length = 111)
	private String location;
	
	@Column(name = "jobRole", nullable = false, length = 55)
	private String jobRole;

	@Column(name = "description", nullable = true, length = 255)
	private String description;
	
	@Column(name = "joinDate")
	private LocalDate joinDate;
	
	@Column(name = "endDate")
	private LocalDate endDate;
	
	// relationship
	
	@ManyToOne
	@JoinColumn(name = "member_id")
	@JsonIgnore
	private MemberAccount memberAccount;
	
}

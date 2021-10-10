package com.astrodust.familyStoryBook_backend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "family_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FamilyAccount {
	
	// properties
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "family_id")
	private int id;
	
	@Column(name = "username", unique = true, nullable = false)
	private String username;
	
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	
	@Column(name = "password", nullable = false)
	@JsonIgnore
	private String password;
	
	@Column(name = "createdDate", nullable = false)
	private LocalDateTime createdDate;
	
	@Column(name = "updatedDate", nullable = true)
	private LocalDateTime updatedDate;
	
	// relationship
	
	@OneToMany(mappedBy = "familyAccount")
	@JsonIgnore
	private List<MemberAccount> memberAccounts = new ArrayList<MemberAccount>();
	
	@OneToMany(mappedBy = "familyAccount")
	@JsonIgnore
	private List<Event> events = new ArrayList<Event>();

	@OneToMany(mappedBy = "familyAccount")
	@JsonIgnore
	private List<MiscellaneousDocument> documents = new ArrayList<>();
	
}

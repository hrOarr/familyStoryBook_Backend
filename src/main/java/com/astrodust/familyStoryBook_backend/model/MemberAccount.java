package com.astrodust.familyStoryBook_backend.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "member_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberAccount {
	
	//  properties
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private int id;
	
	@Column(name = "firstName", length = 255)
	private String firstName;
	
	@Column(name = "lastName", length = 255)
	private String lastName;
	
	@Column(name = "email", length = 255)
	private String email;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "birthDate")
	@Temporal(TemporalType.DATE)
	private Calendar birthDate;
	
	@Column(name = "deathDate")
	@Temporal(TemporalType.DATE)
	private Calendar deathDate;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "createdDate")
	@Temporal(TemporalType.DATE)
	private Calendar createdDate;
	
	@Column(name = "updatedDate")
	@Temporal(TemporalType.DATE)
	private Calendar updatedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	@JsonIgnore
	private MemberAccount parent;
	
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	private Set<MemberAccount> childrens = new HashSet<MemberAccount>();
		
	// relationship
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "family_id")
	@JsonIgnore
	private FamilyAccount familyAccount;
	
	@OneToMany(mappedBy = "memberAccount")
	@JsonIgnore
	private List<MemberEducation> memberEducations = new ArrayList<MemberEducation>();
	
	@OneToMany(mappedBy = "memberAccount")
	@JsonIgnore
	private List<MemberJob> memberJobs = new ArrayList<MemberJob>();
	
	@OneToMany(mappedBy = "memberAccount")
	@JsonIgnore
	private List<Achievement> achievements = new ArrayList<Achievement>();
	
	@ManyToMany(mappedBy = "memberAccounts")
	@JsonIgnore
	private List<Hobby> hobbies = new ArrayList<Hobby>();
	
}

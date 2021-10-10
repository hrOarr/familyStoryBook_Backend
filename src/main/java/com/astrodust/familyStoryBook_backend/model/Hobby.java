package com.astrodust.familyStoryBook_backend.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Hobby")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hobby {
	
	// properties
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hobby_id")
	private int id;
	
	@Column(name = "name", nullable = false, length = 55)
	private String name;
	
	// relationship

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "member_hobby", joinColumns = {@JoinColumn(name="hobby_id")},
			inverseJoinColumns = {@JoinColumn(name="member_id")})
	private List<MemberAccount> memberAccounts = new ArrayList<MemberAccount>();
	
}

package com.astrodust.familyStoryBook_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Achievement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Achievement {
	
	// properties
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "achievement_id")
	private int id;
	
	@Column(name = "title", nullable = false, length = 255)
	private String title;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "addedDate")
	private LocalDateTime addedDate;
	
	@Column(name = "updatedDate")
	private LocalDateTime updatedDate;
	
	// relationship
	
	@ManyToOne
	@JoinColumn(name = "member_id")
	@JsonIgnore
	private MemberAccount memberAccount;

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name = "image_id")
	private ImageModel image;
	
}

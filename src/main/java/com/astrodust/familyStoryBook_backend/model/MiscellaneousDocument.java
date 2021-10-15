package com.astrodust.familyStoryBook_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "miscellaneous_document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MiscellaneousDocument {

    // properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "miscellaneous_id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "addedDate")
    private LocalDateTime addedDate;

    @Column(name = "updatedDate")
    private LocalDateTime updatedDate;

    // relationship
    @ManyToOne
    @JoinColumn(name = "family_id")
    @JsonIgnore
    private FamilyAccount familyAccount;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "miscellaneous_document_image", joinColumns = {@JoinColumn(name="miscellaneous_id")},
            inverseJoinColumns = {@JoinColumn(name="image_id")})
    private List<ImageModel> images = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "miscellaneous_document_file", joinColumns = {@JoinColumn(name="miscellaneous_id")},
            inverseJoinColumns = {@JoinColumn(name="file_id")})
    private List<FileModel> files = new ArrayList<>();
}

package com.astrodust.familyStoryBook_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "image_model")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageModel {

    // properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "picByte", length = 2500000)
    @Lob
    private byte[] picByte;

    // relationship

}

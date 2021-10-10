package com.astrodust.familyStoryBook_backend.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class InsertDocumentDTO {
    private int id;
    private String title;
    private String description;
    List<MultipartFile> images = new ArrayList<>();
    List<MultipartFile> files = new ArrayList<>();
}

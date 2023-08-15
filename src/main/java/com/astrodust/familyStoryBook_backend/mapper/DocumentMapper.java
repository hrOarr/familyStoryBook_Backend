package com.astrodust.familyStoryBook_backend.mapper;

import com.astrodust.familyStoryBook_backend.dto.InsertDocumentDTO;
import com.astrodust.familyStoryBook_backend.utils.ImageCompressionDecom;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.model.FileModel;
import com.astrodust.familyStoryBook_backend.model.ImageModel;
import com.astrodust.familyStoryBook_backend.model.MiscellaneousDocument;
import com.astrodust.familyStoryBook_backend.service.interfaces.FamilyService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class DocumentMapper {

    private final FamilyService familyService;

    public DocumentMapper(FamilyService familyService){
        this.familyService = familyService;
    }

    public MiscellaneousDocument insertDocumentDTOtoEntity(InsertDocumentDTO insertDocumentDTO, FamilyAccount familyAccount) throws IOException {
        MiscellaneousDocument document = new MiscellaneousDocument();
        document.setTitle(insertDocumentDTO.getTitle());
        document.setDescription(insertDocumentDTO.getDescription());
        document.setAddedDate(LocalDateTime.now());
        document.setFamilyAccount(familyAccount);
        for(MultipartFile image:insertDocumentDTO.getImages()){
            ImageModel imageModel = new ImageModel();
            imageModel.setName(image.getOriginalFilename());
            imageModel.setType(image.getContentType());
            imageModel.setPicByte(ImageCompressionDecom.compressBytes(image.getBytes()));
            document.getImages().add(imageModel);
        }
        for(MultipartFile file:insertDocumentDTO.getFiles()){
            FileModel fileModel = new FileModel();
            fileModel.setName(file.getOriginalFilename());
            fileModel.setType(file.getContentType());
            fileModel.setFileByte(ImageCompressionDecom.compressBytes(file.getBytes()));
            document.getFiles().add(fileModel);
        }
        return document;
    }
}

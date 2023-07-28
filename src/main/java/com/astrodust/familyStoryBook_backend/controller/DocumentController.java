package com.astrodust.familyStoryBook_backend.controller;

import com.astrodust.familyStoryBook_backend.dto.InsertDocumentDTO;
import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import com.astrodust.familyStoryBook_backend.helpers.Converter;
import com.astrodust.familyStoryBook_backend.helpers.ImageCompressionDecom;
import com.astrodust.familyStoryBook_backend.model.FileModel;
import com.astrodust.familyStoryBook_backend.model.MiscellaneousDocument;
import com.astrodust.familyStoryBook_backend.service.DocumentService;
import com.astrodust.familyStoryBook_backend.service.FamilyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// changed

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/family/document")
public class DocumentController {
    private static final Logger logger = LogManager.getLogger(DocumentController.class);
    private DocumentService documentService;
    private FamilyService familyService;
    private Converter converter;

    @Autowired
    public DocumentController(DocumentService documentService, FamilyService familyService, Converter converter){
        this.documentService = documentService;
        this.familyService = familyService;
        this.converter = converter;
    }

    @ApiOperation(value = "Get All by Family-Id")
    @GetMapping(value = "/getAllByFamilyId/{familyId}")
    public ResponseEntity<?> getAllByFamilyId(@PathVariable int familyId) throws Exception {
        try {
            List<MiscellaneousDocument> documents = documentService.getAllByFamilyId(familyId);
            return ResponseEntity.status(HttpStatus.OK).body(documents);
        }
        catch (Exception e){
            logger.info("SoA:: exception from getAllByFamilyId() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Save Document")
    @PostMapping(value = "/save/{familyId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> save(@RequestParam("data") String str, @RequestPart(name = "images", required = false) List<MultipartFile> images,
                                  @RequestPart(name = "files", required = false) List<MultipartFile> files, @PathVariable int familyId) throws Exception {
        try {
            logger.info("SoA:: save() method starts------------------->");
            InsertDocumentDTO insertDocumentDTO = new ObjectMapper().readValue(str, InsertDocumentDTO.class);
            for (MultipartFile img : images) {
                insertDocumentDTO.getImages().add(img);
                logger.info(img.getOriginalFilename());
            }
            for (MultipartFile file : files) {
                insertDocumentDTO.getFiles().add(file);
                logger.info(file.getOriginalFilename());
            }
            logger.info("SoA:: intermediate state in save() method-------------->");
            documentService.save(converter.insertDocumentDTOtoDocument(insertDocumentDTO, familyService.getById(familyId)));
            return ResponseEntity.status(HttpStatus.OK).body(familyId);
        }
        catch (Exception e){
            logger.info("SoA:: exception from save() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) throws Exception {
        try {
            MiscellaneousDocument document = documentService.getById(id);
            if (document == null) {
                throw new ResourceNotFoundException("Resource Not Found with id = " + id);
            }
            return ResponseEntity.status(HttpStatus.OK).body(document);
        }
        catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(e.getLocalizedMessage());
        }
        catch (Exception e){
            logger.info("SoA:: exception from getById() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Download file from document")
    @GetMapping(value = "/downloadFile/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable int fileId) throws Exception {
        try {
            FileModel fileModel = documentService.getSingleFileByFileId(fileId);
            if (fileModel == null) {
                throw new ResourceNotFoundException("Resource Not Found with id = " + fileId);
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.parseMediaType(fileModel.getType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment:filename=\"" + fileModel.getName() + "\"")
                    .body(new ByteArrayResource(ImageCompressionDecom.decompressBytes(fileModel.getFileByte())));
        }
        catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(e.getLocalizedMessage());
        }
        catch (Exception e){
            logger.info("SoA:: exception from downloadFile() method---------------->", e);
            throw new Exception("Something went wrong. Please try again");
        }
    }

    @ApiOperation(value = "Delete Document")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        int cnt = documentService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(cnt + " row(s) deleted");
    }
}

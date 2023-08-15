package com.astrodust.familyStoryBook_backend.controller;

import com.astrodust.familyStoryBook_backend.exception.ResourceNotFoundException;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.model.FileModel;
import com.astrodust.familyStoryBook_backend.model.MiscellaneousDocument;
import com.astrodust.familyStoryBook_backend.service.interfaces.DocumentService;
import com.astrodust.familyStoryBook_backend.service.interfaces.FamilyService;
import com.astrodust.familyStoryBook_backend.utils.AuthenticatedUser;
import com.astrodust.familyStoryBook_backend.utils.ImageCompressionDecom;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {
    private final DocumentService documentService;
    private final FamilyService familyService;
    private final AuthenticatedUser authenticatedUser;

    @Autowired
    public DocumentController(DocumentService documentService, FamilyService familyService, AuthenticatedUser authenticatedUser){
        this.documentService = documentService;
        this.familyService = familyService;
        this.authenticatedUser = authenticatedUser;
    }

    @ApiOperation(value = "Get All by Family-Id")
    @GetMapping(value = "/")
    public ResponseEntity<?> getAllByFamilyId(@RequestParam("familyId") Integer familyId) throws Exception {
        List<MiscellaneousDocument> documents = documentService.getAllByFamilyId(familyId);
        return ResponseEntity.status(HttpStatus.OK).body(documents);
    }

    @ApiOperation(value = "Save Document")
    @PostMapping(value = "/", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> save(@RequestParam("data") String data, @RequestPart(name = "images", required = false) List<MultipartFile> images,
                                  @RequestPart(name = "files", required = false) List<MultipartFile> files) throws Exception {
        FamilyAccount familyAccount = authenticatedUser.get();
        documentService.save(data, images, files, familyAccount);
        return ResponseEntity.status(HttpStatus.OK).body("Successful");
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) throws Exception {
        MiscellaneousDocument document = documentService.getById(id);
        if (document == null) {
            throw new ResourceNotFoundException("Resource Not Found with id = " + id);
        }
        return ResponseEntity.status(HttpStatus.OK).body(document);
    }

    @ApiOperation(value = "Download file from document")
    @GetMapping(value = "/download/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable int fileId) {
        FileModel fileModel = documentService.getByFileId(fileId);
        if (fileModel == null) {
            throw new ResourceNotFoundException("Resource Not Found with id = " + fileId);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(fileModel.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment:filename=\"" + fileModel.getName() + "\"")
                .body(new ByteArrayResource(ImageCompressionDecom.decompressBytes(fileModel.getFileByte())));
    }

    @ApiOperation(value = "Delete Document")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        int count = documentService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(count + " row(s) deleted");
    }
}

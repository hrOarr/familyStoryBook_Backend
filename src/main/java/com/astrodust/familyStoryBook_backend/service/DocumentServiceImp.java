package com.astrodust.familyStoryBook_backend.service;

import com.astrodust.familyStoryBook_backend.dao.interfaces.DocumentDao;
import com.astrodust.familyStoryBook_backend.dto.InsertDocumentDTO;
import com.astrodust.familyStoryBook_backend.mapper.DocumentMapper;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.model.FileModel;
import com.astrodust.familyStoryBook_backend.model.MiscellaneousDocument;
import com.astrodust.familyStoryBook_backend.service.interfaces.DocumentService;
import com.astrodust.familyStoryBook_backend.service.interfaces.FamilyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentServiceImp implements DocumentService {

    private final DocumentDao documentDao;
    private final DocumentMapper documentMapper;
    private final FamilyService familyService;

    @Autowired
    public DocumentServiceImp(DocumentDao documentDao, DocumentMapper documentMapper, FamilyService familyService){
        this.documentDao = documentDao;
        this.documentMapper = documentMapper;
        this.familyService = familyService;
    }

    @Override
    public void save(String data, List<MultipartFile> images, List<MultipartFile> files, FamilyAccount familyAccount) throws IOException {
        InsertDocumentDTO insertDocumentDTO = new ObjectMapper().readValue(data, InsertDocumentDTO.class);
        for (MultipartFile img : images) {
            insertDocumentDTO.getImages().add(img);
        }
        for (MultipartFile file : files) {
            insertDocumentDTO.getFiles().add(file);
        }
        MiscellaneousDocument miscellaneousDocument = documentMapper.insertDocumentDTOtoEntity(insertDocumentDTO, familyAccount);
        documentDao.save(miscellaneousDocument);
    }

    @Override
    public void update(MiscellaneousDocument miscellaneousDocument) {
        documentDao.update(miscellaneousDocument);
    }

    @Override
    public int delete(int id) {
        return documentDao.delete(id);
    }

    @Override
    public MiscellaneousDocument getById(int id) {
        return documentDao.getById(id);
    }

    @Override
    public List<MiscellaneousDocument> getAllByFamilyId(int familyId) {
        return documentDao.getAllByFamilyId(familyId);
    }

    @Override
    public List<MiscellaneousDocument> getAll() {
        return documentDao.getAll();
    }

    @Override
    public FileModel getByFileId(int fileId) {
        return documentDao.getSingleFileByFileId(fileId);
    }
}

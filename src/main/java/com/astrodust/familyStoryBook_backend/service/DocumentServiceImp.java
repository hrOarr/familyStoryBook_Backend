package com.astrodust.familyStoryBook_backend.service;

import com.astrodust.familyStoryBook_backend.dao.DocumentDao;
import com.astrodust.familyStoryBook_backend.model.FileModel;
import com.astrodust.familyStoryBook_backend.model.MiscellaneousDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImp implements DocumentService{

    private DocumentDao documentDao;

    @Autowired
    public DocumentServiceImp(DocumentDao documentDao){
        this.documentDao = documentDao;
    }

    @Override
    public void save(MiscellaneousDocument miscellaneousDocument) {
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
    public FileModel getSingleFileByFileId(int fileId) {
        return documentDao.getSingleFileByFileId(fileId);
    }
}

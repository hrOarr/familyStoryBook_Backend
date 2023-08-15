package com.astrodust.familyStoryBook_backend.service.interfaces;

import com.astrodust.familyStoryBook_backend.model.FamilyAccount;
import com.astrodust.familyStoryBook_backend.model.FileModel;
import com.astrodust.familyStoryBook_backend.model.MiscellaneousDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {
    void save(String data, List<MultipartFile> images, List<MultipartFile> files, FamilyAccount familyAccount) throws IOException;
    void update(MiscellaneousDocument miscellaneousDocument);
    int delete(int id);
    MiscellaneousDocument getById(int id);
    List<MiscellaneousDocument> getAllByFamilyId(int familyId);
    List<MiscellaneousDocument> getAll();
    FileModel getByFileId(int fileId);
}

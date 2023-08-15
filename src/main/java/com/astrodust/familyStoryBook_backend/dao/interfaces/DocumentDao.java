package com.astrodust.familyStoryBook_backend.dao.interfaces;

import com.astrodust.familyStoryBook_backend.model.FileModel;
import com.astrodust.familyStoryBook_backend.model.MiscellaneousDocument;

import java.util.List;

public interface DocumentDao {
    public void save(MiscellaneousDocument miscellaneousDocument);
    public void update(MiscellaneousDocument miscellaneousDocument);
    public int delete(int id);
    public MiscellaneousDocument getById(int id);
    public List<MiscellaneousDocument> getAllByFamilyId(int familyId);
    public List<MiscellaneousDocument> getAll();
    public FileModel getSingleFileByFileId(int fileId);
}

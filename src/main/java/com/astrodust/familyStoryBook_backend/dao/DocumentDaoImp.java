package com.astrodust.familyStoryBook_backend.dao;

import com.astrodust.familyStoryBook_backend.dao.interfaces.DocumentDao;
import com.astrodust.familyStoryBook_backend.model.FileModel;
import com.astrodust.familyStoryBook_backend.model.MiscellaneousDocument;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class DocumentDaoImp implements DocumentDao {

    private SessionFactory sessionFactory;

    @Autowired
    public DocumentDaoImp(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(MiscellaneousDocument miscellaneousDocument) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(miscellaneousDocument);
    }

    @Override
    public void update(MiscellaneousDocument miscellaneousDocument) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(miscellaneousDocument);
    }

    @Override
    public int delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        MiscellaneousDocument document = this.getById(id);
        if(document!=null){
            session.delete(document);
            return 1;
        }
        return 0;
    }

    @Override
    public MiscellaneousDocument getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(MiscellaneousDocument.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MiscellaneousDocument> getAllByFamilyId(int familyId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT md FROM MiscellaneousDocument md INNER JOIN md.familyAccount fa WHERE fa.id=:id");
        query.setParameter("id", familyId);
        List<MiscellaneousDocument> documents = query.getResultList();
        return documents;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MiscellaneousDocument> getAll() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM MiscellaneousDocument");
        List<MiscellaneousDocument> documents = query.getResultList();
        return documents;
    }

    @Override
    @SuppressWarnings("unchecked")
    public FileModel getSingleFileByFileId(int fileId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT fm FROM MiscellaneousDocument md INNER JOIN md.files fm WHERE fm.id=:id");
        query.setParameter("id", fileId);
        List<FileModel> fileModels = query.getResultList();
        if(fileModels==null||fileModels.isEmpty()){
            return null;
        }
        return fileModels.get(0);
    }
}

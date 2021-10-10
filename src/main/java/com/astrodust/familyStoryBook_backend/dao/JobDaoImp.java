package com.astrodust.familyStoryBook_backend.dao;

import com.astrodust.familyStoryBook_backend.model.MemberJob;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class JobDaoImp implements JobDao {

    private SessionFactory sessionFactory;

    @Autowired
    public JobDaoImp(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public MemberJob save(MemberJob memberJob) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(memberJob);
        return this.getById(memberJob.getId());
    }

    @Override
    public MemberJob getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(MemberJob.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MemberJob> getByMemberId(int memberId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT mj FROM MemberJob mj INNER JOIN mj.memberAccount ma WHERE ma.id=:id";
        Query query = session.createQuery(hql);
        query.setParameter("id", memberId);
        List<MemberJob> memberJobs = query.getResultList();
        return memberJobs;
    }

    @Override
    public MemberJob update(MemberJob memberJob) {
        Session session = sessionFactory.getCurrentSession();
        session.update(memberJob);
        return this.getById(memberJob.getId());
    }

    @Override
    public int delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("DELETE FROM MemberJob mj WHERE mj.id=:id");
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MemberJob> getAll() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM MemberJob");
        List<MemberJob> memberJobs = query.getResultList();
        return memberJobs;
    }
}

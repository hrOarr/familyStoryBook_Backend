package com.astrodust.familyStoryBook_backend.dao;

import com.astrodust.familyStoryBook_backend.model.Achievement;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class AchievementDaoImp implements  AchievementDao{

    private SessionFactory sessionFactory;

    @Autowired
    public AchievementDaoImp(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Achievement save(Achievement achievement) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(achievement);
        return this.getById(achievement.getId());
    }

    @Override
    public Achievement update(Achievement achievement) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(achievement);
        return this.getById(achievement.getId());
    }

    @Override
    public Achievement getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Achievement achievement = session.get(Achievement.class, id);
        return achievement;
    }

    @Override
    public int delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        Achievement achievement = this.getById(id);
        if(achievement!=null){
            session.delete(achievement);
            return 1;
        }
        else return 0;
//        Query query = session.createQuery("DELETE FROM Achievement a WHERE a.id=:id");
//        query.setParameter("id", id);
//        return query.executeUpdate();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Achievement> getAll() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Achievement");
        List<Achievement> achievements = query.getResultList();
        return achievements;
    }

    @Override
    public List<Achievement> getAllByMemberId(int memberId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT a FROM Achievement a INNER JOIN a.memberAccount ma WHERE ma.id=:id");
        query.setParameter("id", memberId);
        List<Achievement> achievements = query.getResultList();
        return achievements;
    }
}

package com.astrodust.familyStoryBook_backend.dao;

import java.util.List;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.astrodust.familyStoryBook_backend.model.MemberEducation;

@Repository
@Transactional
public class EducationDaoImp implements EducationDao {
	
	private SessionFactory sessionFactory;
	
	@Autowired
	public EducationDaoImp(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void save(MemberEducation education) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(education);
	}

	@Override
	public void update(MemberEducation education) {
		Session session = sessionFactory.getCurrentSession();
		session.merge(education);
	}

	@Override
	public int delete(int id) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("DELETE FROM MemberEducation me WHERE me.id=:id");
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	@Override
	public MemberEducation getById(int id) {
		Session session = sessionFactory.getCurrentSession();
		return session.get(MemberEducation.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MemberEducation> getAll() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM MemberEducation");
		List<MemberEducation> educations = query.getResultList();
		return educations;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MemberEducation> getByMemberId(int mid) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT me FROM MemberEducation me INNER JOIN me.memberAccount ma WHERE ma.id=:id");
		query.setParameter("id", mid);
		List<MemberEducation> educations = query.getResultList();
		return educations;
	}

}

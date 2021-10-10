package com.astrodust.familyStoryBook_backend.dao;

import java.util.List;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.astrodust.familyStoryBook_backend.model.FamilyAccount;

@Repository
@Transactional
public class FamilyDaoImp implements FamilyDao {
	
	private SessionFactory sessionFactory;
	
	@Autowired
	public FamilyDaoImp(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void save(FamilyAccount account) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(account);
	}

	@Override
	public void update(FamilyAccount account) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(account);
	}

	@Override
	public FamilyAccount getById(int id) {
		Session session = sessionFactory.getCurrentSession();
		return session.get(FamilyAccount.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public FamilyAccount getByEmail(String email) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "FROM FamilyAccount fa WHERE fa.email =: email";
		Query query = session.createQuery(hql);
		query.setParameter("email", email);
		List<FamilyAccount> accounts = query.getResultList();
		if(accounts==null || accounts.isEmpty()) {
			return null;
		}
		return accounts.get(0);
	}

}

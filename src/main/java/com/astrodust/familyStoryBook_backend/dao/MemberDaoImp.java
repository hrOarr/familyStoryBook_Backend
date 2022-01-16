package com.astrodust.familyStoryBook_backend.dao;

import java.util.List;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.astrodust.familyStoryBook_backend.model.MemberAccount;

@Repository
@Transactional
public class MemberDaoImp implements MemberDao {
	
	private SessionFactory sessionFactory;
	
	@Autowired
	public MemberDaoImp(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void save(MemberAccount account) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(account);
	}

	@Override
	public void update(MemberAccount account) {
		Session session = sessionFactory.getCurrentSession();
		session.update(account);
	}

	@Override
	public MemberAccount getById(int id) {
		Session session = sessionFactory.getCurrentSession();
		return session.get(MemberAccount.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public MemberAccount getByEmail(String email) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "FROM MemberAccount ma WHERE ma.email =: email";
		Query query = session.createQuery(hql);
		query.setParameter("email", email);
		List<MemberAccount> accounts = query.getResultList();
		if(accounts==null ||accounts.isEmpty()) {
			return null;
		}
		return accounts.get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MemberAccount> getAllChildsByParent(int parent_id) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "FROM MemberAccount ma WHERE ma.parent_id =: parent_id";
		Query query = session.createQuery(hql);
		query.setParameter("parent_id", parent_id);
		return (List<MemberAccount>)query.getResultList();
	}

	@Override
	public MemberAccount getParentByChild(int child_id) {
		
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MemberAccount> getAllMembersByFid(int fid) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT ma FROM MemberAccount ma INNER JOIN ma.familyAccount fa WHERE fa.id=:id");
		query.setParameter("id", fid);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public MemberAccount getRootByFid(int f_id) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "SELECT ma FROM MemberAccount ma INNER JOIN ma.familyAccount fa WHERE fa.id=:f_id AND ma.parent.id=null";
		Query query = session.createQuery(hql);
		query.setParameter("f_id", f_id);
		List<MemberAccount> accounts = query.getResultList();
		if(accounts==null||accounts.isEmpty()) {
			return null;
		}
		return accounts.get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public MemberAccount getByFamilyIdAndId(int id, int fid) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "SELECT ma FROM MemberAccount ma INNER JOIN ma.familyAccount fa WHERE ma.id=:id AND fa.id=:fid";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("fid", fid);
		List<MemberAccount> accounts = query.getResultList();
		if(accounts==null||accounts.isEmpty()) {
			return null;
		}
		return accounts.get(0);
	}

}

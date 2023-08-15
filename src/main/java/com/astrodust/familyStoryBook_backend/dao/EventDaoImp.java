package com.astrodust.familyStoryBook_backend.dao;

import java.util.List;

import javax.persistence.Query;
import javax.transaction.Transactional;

import com.astrodust.familyStoryBook_backend.dao.interfaces.EventDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.astrodust.familyStoryBook_backend.model.Event;

@Repository
@Transactional
public class EventDaoImp implements EventDao {
	
	private SessionFactory sessionFactory;
	
	@Autowired
	public EventDaoImp(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void save(Event event) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(event);
	}

	@Override
	public void update(Event event) {
		Session session = sessionFactory.getCurrentSession();
		session.update(event);
	}

	@Override
	public int delete(int id) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "DELETE FROM Event e WHERE e.id=:id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	@Override
	public Event getById(int id) {
		Session session = sessionFactory.getCurrentSession();
		return session.get(Event.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Event> getAllEvents() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Event");
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Event> getAllByFamilyId(int fid) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT e FROM Event e INNER JOIN e.familyAccount fa WHERE fa.id=:id");
		query.setParameter("id", fid);
		return query.getResultList();
	}

}

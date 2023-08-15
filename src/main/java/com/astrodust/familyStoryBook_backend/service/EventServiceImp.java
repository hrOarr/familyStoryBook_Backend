package com.astrodust.familyStoryBook_backend.service;

import java.util.List;

import com.astrodust.familyStoryBook_backend.service.interfaces.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.astrodust.familyStoryBook_backend.dao.interfaces.EventDao;
import com.astrodust.familyStoryBook_backend.model.Event;

@Service
public class EventServiceImp implements EventService {
	
	private EventDao eventDao;
	
	@Autowired
	public EventServiceImp(EventDao eventDao) {
		this.eventDao = eventDao;
	}

	@Override
	public void save(Event event) {
		eventDao.save(event);
	}

	@Override
	public void update(Event event) {
		eventDao.update(event);
	}

	@Override
	public int delete(int id) {
		return eventDao.delete(id);
	}

	@Override
	public Event getById(int id) {
		return eventDao.getById(id);
	}

	@Override
	public List<Event> getAllEvents() {
		return eventDao.getAllEvents();
	}

	@Override
	public List<Event> getAllByFamilyId(int fid) {
		return eventDao.getAllByFamilyId(fid);
	}

}

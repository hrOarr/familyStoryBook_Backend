package com.astrodust.familyStoryBook_backend.dao;

import java.util.List;

import com.astrodust.familyStoryBook_backend.model.Event;

public interface EventDao {
	public void save(Event event);
	public void update(Event event);
	public int delete(int id);
	public Event getById(int id);
	public List<Event> getAllByFamilyId(int fid);
	public List<Event> getAllEvents();
}

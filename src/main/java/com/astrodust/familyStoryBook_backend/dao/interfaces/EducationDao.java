package com.astrodust.familyStoryBook_backend.dao.interfaces;

import java.util.List;

import com.astrodust.familyStoryBook_backend.model.MemberEducation;

public interface EducationDao {
	public void save(MemberEducation education);
	public void update(MemberEducation education);
	public int delete(int id);
	public MemberEducation getById(int id);
	public List<MemberEducation> getAll();
	public List<MemberEducation> getByMemberId(int mid);
}

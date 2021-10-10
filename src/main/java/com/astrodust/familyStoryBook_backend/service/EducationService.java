package com.astrodust.familyStoryBook_backend.service;

import java.util.List;

import com.astrodust.familyStoryBook_backend.model.MemberEducation;

public interface EducationService {
	public void save(List<MemberEducation> educations);
	public void updateList(List<MemberEducation> educations);
	public void updateOne(MemberEducation education);
	public int delete(int id);
	public MemberEducation getById(int id);
	public List<MemberEducation> getAll();
	public List<MemberEducation> getByMemberId(int mid);
}

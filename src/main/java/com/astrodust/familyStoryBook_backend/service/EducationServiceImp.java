package com.astrodust.familyStoryBook_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.astrodust.familyStoryBook_backend.dao.EducationDao;
import com.astrodust.familyStoryBook_backend.model.MemberEducation;

@Service
public class EducationServiceImp implements EducationService {

	private EducationDao educationDao;
	
	@Autowired
	public EducationServiceImp(EducationDao educationDao) {
		this.educationDao = educationDao;
	}
	
	@Override
	public void save(List<MemberEducation> educations) {
		for(MemberEducation education:educations) {
			educationDao.save(education);
		}
	}

	@Override
	public void updateList(List<MemberEducation> educations) {
		for(MemberEducation education:educations) {
			System.out.println(education.getId());
			educationDao.update(education);
		}
	}

	@Override
	public void updateOne(MemberEducation education) {
		educationDao.update(education);
	}

	@Override
	public int delete(int id) {
		return educationDao.delete(id);
	}

	@Override
	public MemberEducation getById(int id) {
		return educationDao.getById(id);
	}

	@Override
	public List<MemberEducation> getAll() {
		return educationDao.getAll();
	}

	@Override
	public List<MemberEducation> getByMemberId(int mid) {
		return educationDao.getByMemberId(mid);
	}

}

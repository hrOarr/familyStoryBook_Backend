package com.astrodust.familyStoryBook_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.astrodust.familyStoryBook_backend.dao.FamilyDao;
import com.astrodust.familyStoryBook_backend.model.FamilyAccount;

@Service
public class FamilyServiceImp implements FamilyService {
	
	private FamilyDao familyDao;
	
	@Autowired
	public FamilyServiceImp(FamilyDao familyDao) {
		this.familyDao = familyDao;
	}
	
	@Override
	public void save(FamilyAccount account) {
		familyDao.save(account);
	}

	@Override
	public void update(FamilyAccount account) {
		familyDao.update(account);
	}

	@Override
	public FamilyAccount getById(int id) {
		return familyDao.getById(id);
	}

	@Override
	public FamilyAccount getByEmail(String email) {
		return familyDao.getByEmail(email);
	}

}

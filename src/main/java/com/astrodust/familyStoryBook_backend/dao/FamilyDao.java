package com.astrodust.familyStoryBook_backend.dao;

import com.astrodust.familyStoryBook_backend.model.FamilyAccount;

public interface FamilyDao {
	public void save(FamilyAccount account);
	public void update(FamilyAccount account);
	public FamilyAccount getById(int id);
	public FamilyAccount getByEmail(String email);
}

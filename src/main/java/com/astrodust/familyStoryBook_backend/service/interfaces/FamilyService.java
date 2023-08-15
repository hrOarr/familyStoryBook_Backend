package com.astrodust.familyStoryBook_backend.service.interfaces;

import com.astrodust.familyStoryBook_backend.model.FamilyAccount;

public interface FamilyService {
	public void save(FamilyAccount account);
	public void update(FamilyAccount account);
	public FamilyAccount getById(int id);
	public FamilyAccount getByEmail(String email);
	public FamilyAccount getByUsername(String username);
	public int deleteById(int id);
}

package com.astrodust.familyStoryBook_backend.service.interfaces;

import java.util.List;

import com.astrodust.familyStoryBook_backend.model.MemberAccount;

public interface MemberService {
	public void save(MemberAccount account);
	public void update(MemberAccount account);
	public MemberAccount getById(int id);
	public MemberAccount getRootByFid(int f_id);
	public MemberAccount getByEmail(String email);
	public MemberAccount getByFamilyIdAndId(int id,int fid);
	public List<MemberAccount> getAllChildsByParent(int parent_id);
	public MemberAccount getParentByChild(int child_id);
	public List<MemberAccount> getAllMembersByFid(int fid);
}

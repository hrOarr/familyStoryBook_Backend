package com.astrodust.familyStoryBook_backend.service;

import java.util.List;

import com.astrodust.familyStoryBook_backend.service.interfaces.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.astrodust.familyStoryBook_backend.dao.interfaces.MemberDao;
import com.astrodust.familyStoryBook_backend.model.MemberAccount;

@Service
public class MemberServiceImp implements MemberService {
		
	private MemberDao memberDao;
	
	@Autowired
	public MemberServiceImp(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	
	@Override
	public void save(MemberAccount account) {
		memberDao.save(account);
	}

	@Override
	public void update(MemberAccount account) {
		memberDao.update(account);
	}

	@Override
	public MemberAccount getById(int id) {
		return memberDao.getById(id);
	}

	@Override
	public MemberAccount getByEmail(String email) {
		return memberDao.getByEmail(email);
	}

	@Override
	public List<MemberAccount> getAllChildsByParent(int parent_id) {
		return memberDao.getAllChildsByParent(parent_id);
	}

	@Override
	public MemberAccount getParentByChild(int child_id) {
		return memberDao.getParentByChild(child_id);
	}

	@Override
	public List<MemberAccount> getAllMembersByFid(int fid) {
		return memberDao.getAllMembersByFid(fid);
	}

	@Override
	public MemberAccount getRootByFid(int f_id) {
		return memberDao.getRootByFid(f_id);
	}

	@Override
	public MemberAccount getByFamilyIdAndId(int id, int fid) {
		return memberDao.getByFamilyIdAndId(id, fid);
	}

}

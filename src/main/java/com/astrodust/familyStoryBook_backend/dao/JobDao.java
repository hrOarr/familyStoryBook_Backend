package com.astrodust.familyStoryBook_backend.dao;

import com.astrodust.familyStoryBook_backend.model.MemberJob;

import java.util.List;

public interface JobDao {
    public MemberJob save(MemberJob memberJob);
    public MemberJob getById(int id);
    public List<MemberJob> getByMemberId(int memberId);
    public MemberJob update(MemberJob memberJob);
    public int delete(int id);
    public List<MemberJob> getAll();
}

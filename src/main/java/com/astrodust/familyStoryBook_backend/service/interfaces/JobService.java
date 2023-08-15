package com.astrodust.familyStoryBook_backend.service.interfaces;

import com.astrodust.familyStoryBook_backend.model.MemberJob;

import java.util.List;

public interface JobService {
    public List<MemberJob> save(List<MemberJob> memberJobs);
    public MemberJob getById(int id);
    public List<MemberJob> getByMemberId(int memberId);
    public MemberJob update(MemberJob memberJob);
    public int delete(int id);
    public List<MemberJob> getAll();
}

package com.astrodust.familyStoryBook_backend.service;

import com.astrodust.familyStoryBook_backend.dao.JobDao;
import com.astrodust.familyStoryBook_backend.model.MemberJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobServiceImp implements JobService{

    private JobDao jobDao;

    @Autowired
    public JobServiceImp(JobDao jobDao){
        this.jobDao = jobDao;
    }

    @Override
    public List<MemberJob> save(List<MemberJob> memberJobs) {
        List<MemberJob> memberJobList = new ArrayList<>();
        for(MemberJob memberJob:memberJobs){
            memberJobList.add(jobDao.save(memberJob));
        }
        return memberJobList;
    }

    @Override
    public MemberJob getById(int id) {
        return jobDao.getById(id);
    }

    @Override
    public List<MemberJob> getByMemberId(int memberId) {
        return jobDao.getByMemberId(memberId);
    }

    @Override
    public MemberJob update(MemberJob memberJob) {
        return jobDao.update(memberJob);
    }

    @Override
    public int delete(int id) {
        return jobDao.delete(id);
    }

    @Override
    public List<MemberJob> getAll() {
        return jobDao.getAll();
    }
}

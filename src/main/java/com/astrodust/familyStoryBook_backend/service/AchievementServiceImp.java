package com.astrodust.familyStoryBook_backend.service;

import com.astrodust.familyStoryBook_backend.dao.AchievementDao;
import com.astrodust.familyStoryBook_backend.model.Achievement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AchievementServiceImp implements AchievementService{

    private AchievementDao achievementDao;

    @Autowired
    public AchievementServiceImp(AchievementDao achievementDao){
        this.achievementDao = achievementDao;
    }

    @Override
    public List<Achievement> save(List<Achievement> achievements) {
        List<Achievement> achievementList = new ArrayList<>();
        for(Achievement achievement:achievements){
            achievementList.add(achievementDao.save(achievement));
        }
        return achievementList;
    }

    @Override
    public Achievement update(Achievement achievement) {
        return achievementDao.update(achievement);
    }

    @Override
    public Achievement getById(int id) {
        return achievementDao.getById(id);
    }

    @Override
    public int delete(int id) {
        return achievementDao.delete(id);
    }

    @Override
    public List<Achievement> getAll() {
        return achievementDao.getAll();
    }

    @Override
    public List<Achievement> getAllByMemberId(int memberId) {
        return achievementDao.getAllByMemberId(memberId);
    }
}

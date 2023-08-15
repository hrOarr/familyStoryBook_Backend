package com.astrodust.familyStoryBook_backend.dao.interfaces;

import com.astrodust.familyStoryBook_backend.model.Achievement;

import java.util.List;

public interface AchievementDao {
    public Achievement save(Achievement achievement);
    public Achievement update(Achievement achievement);
    public Achievement getById(int id);
    public int delete(int id);
    public List<Achievement> getAll();
    public List<Achievement> getAllByMemberId(int memberId);
}

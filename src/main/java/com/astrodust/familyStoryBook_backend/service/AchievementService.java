package com.astrodust.familyStoryBook_backend.service;

import com.astrodust.familyStoryBook_backend.model.Achievement;

import java.util.List;

public interface AchievementService {
    public List<Achievement> save(List<Achievement> achievements);
    public Achievement update(Achievement achievement);
    public Achievement getById(int id);
    public int delete(int id);
    public List<Achievement> getAll();
    public List<Achievement> getAllByMemberId(int memberId);
}

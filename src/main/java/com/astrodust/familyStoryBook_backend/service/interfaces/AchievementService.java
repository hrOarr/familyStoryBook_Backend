package com.astrodust.familyStoryBook_backend.service.interfaces;

import com.astrodust.familyStoryBook_backend.dto.AchievementDTO;
import com.astrodust.familyStoryBook_backend.dto.InsertAchievementDTO;
import com.astrodust.familyStoryBook_backend.dto.UpdateAchievementDTO;
import com.astrodust.familyStoryBook_backend.model.Achievement;

import java.io.IOException;
import java.util.List;

public interface AchievementService {
    List<AchievementDTO> save(InsertAchievementDTO insertAchievementDTO, int memberId) throws IOException;
    AchievementDTO update(UpdateAchievementDTO updateAchievementDTO, int memberId) throws IOException;
    Achievement getById(int id);
    int delete(int id);
    List<Achievement> getAll();
    List<AchievementDTO> getAllByMemberId(int memberId);
}

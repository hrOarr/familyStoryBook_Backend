package com.astrodust.familyStoryBook_backend.service;

import com.astrodust.familyStoryBook_backend.dao.interfaces.AchievementDao;
import com.astrodust.familyStoryBook_backend.dto.AchievementDTO;
import com.astrodust.familyStoryBook_backend.dto.InsertAchievementDTO;
import com.astrodust.familyStoryBook_backend.dto.UpdateAchievementDTO;
import com.astrodust.familyStoryBook_backend.mapper.AchievementMapper;
import com.astrodust.familyStoryBook_backend.model.Achievement;
import com.astrodust.familyStoryBook_backend.model.MemberAccount;
import com.astrodust.familyStoryBook_backend.service.interfaces.AchievementService;
import com.astrodust.familyStoryBook_backend.service.interfaces.MemberService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AchievementServiceImp implements AchievementService {

    private final AchievementDao achievementDao;
    private final AchievementMapper achievementMapper;
    private final MemberService memberService;

    public AchievementServiceImp(AchievementDao achievementDao, AchievementMapper achievementMapper, MemberService memberService){
        this.achievementDao = achievementDao;
        this.achievementMapper = achievementMapper;
        this.memberService = memberService;
    }

    @Override
    public List<AchievementDTO> save(InsertAchievementDTO insertAchievementDTO, int memberId) throws IOException {
        List<Achievement> achievements = achievementMapper.toEntity(insertAchievementDTO, memberService.getById(memberId));
        List<AchievementDTO> achievementDTOS = new ArrayList<>();
        for(Achievement achievement:achievements){
            achievement = achievementDao.save(achievement);
            achievementDTOS.add(achievementMapper.toDto(achievement));
        }
        return achievementDTOS;
    }

    @Override
    public AchievementDTO update(UpdateAchievementDTO updateAchievementDTO, int memberId) throws IOException {
        MemberAccount memberAccount = memberService.getById(memberId);
        Achievement achievement = achievementMapper.updateAchievementDTOtoEntity(updateAchievementDTO, memberAccount);
        return achievementMapper.toDto(achievementDao.update(achievement));
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
    public List<AchievementDTO> getAllByMemberId(int memberId) {
        List<Achievement> achievements = achievementDao.getAllByMemberId(memberId);
        return achievementMapper.toDto(achievements);
    }
}

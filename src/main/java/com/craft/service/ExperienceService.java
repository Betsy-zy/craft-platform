package com.craft.service;

import com.craft.entity.Experience;
import com.craft.mapper.ExperienceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExperienceService {
    @Autowired
    private ExperienceMapper experienceMapper;

    public Experience create(Experience experience) {
        experience.setCreateTime(new Date());
        experience.setStatus("pending");
        experienceMapper.insert(experience);
        return experience;
    }

    public List<Experience> findAll() {
        return experienceMapper.findAll();
    }

    public List<Experience> findActiveExperiences() {
        return experienceMapper.findActiveExperiences();
    }

    public Experience findById(Long id) {
        return experienceMapper.findById(id);
    }

    public List<Experience> findByCraftsmanId(Long craftsmanId) {
        return experienceMapper.findByCraftsmanId(craftsmanId);
    }

    public Experience update(Experience experience) {
        experienceMapper.update(experience);
        return experience;
    }

    public void updateStatus(Long id, String status) {
        Experience experience = experienceMapper.findById(id);
        if (experience != null) {
            experience.setStatus(status);
            experienceMapper.update(experience);
        }
    }

    public void delete(Long id) {
        experienceMapper.deleteById(id);
    }
}
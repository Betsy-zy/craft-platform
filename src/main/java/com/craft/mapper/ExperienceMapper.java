package com.craft.mapper;

import com.craft.entity.Experience;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExperienceMapper {
    int insert(Experience experience);
    Experience findById(Long id);
    List<Experience> findAll();
    List<Experience> findActiveExperiences();
    List<Experience> findByCraftsmanId(Long craftsmanId);
    int update(Experience experience);
    int deleteById(Long id);
}
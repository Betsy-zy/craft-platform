package com.craft.mapper;

import com.craft.entity.Work;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WorkMapper {
    int insert(Work work);
    Work findById(Long id);
    List<Work> findAll();
    List<Work> findActiveWorks();
    List<Work> findByCraftsmanId(Long craftsmanId);
    int update(Work work);
    int deleteById(Long id);
}
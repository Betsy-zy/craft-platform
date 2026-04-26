package com.craft.mapper;

import com.craft.entity.Craftsman;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CraftsmanMapper {
    int insert(Craftsman craftsman);
    Craftsman findById(Long id);
    List<Craftsman> findAll();
    List<Craftsman> findByStatus(String status);
    Craftsman findByUserId(Long userId);
    int update(Craftsman craftsman);
    int deleteById(Long id);
}
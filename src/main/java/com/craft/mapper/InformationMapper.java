package com.craft.mapper;

import com.craft.entity.Information;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InformationMapper {
    int insert(Information information);
    Information findById(Long id);
    List<Information> findAll();
    int update(Information information);
    int deleteById(Long id);
}
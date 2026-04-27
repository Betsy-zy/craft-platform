package com.craft.mapper;

import com.craft.entity.CollectionItem;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CollectionMapper {
    List<CollectionItem> findAll();
    CollectionItem findById(Long id);
    List<CollectionItem> findByUserId(Long userId);
    CollectionItem findByUserIdAndTargetIdAndType(Long userId, Long targetId, String targetType);
    void insert(CollectionItem collection);
    void delete(Long id);
    void deleteByUserIdAndTargetIdAndType(Long userId, Long targetId, String targetType);
}
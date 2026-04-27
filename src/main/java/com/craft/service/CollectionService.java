package com.craft.service;

import com.craft.entity.CollectionItem;
import java.util.List;

public interface CollectionService {
    List<CollectionItem> findAll();
    CollectionItem findById(Long id);
    List<CollectionItem> findByUserId(Long userId);
    CollectionItem findByUserIdAndTargetIdAndType(Long userId, Long targetId, String targetType);
    void insert(CollectionItem collection);
    void delete(Long id);
    void deleteByUserIdAndTargetIdAndType(Long userId, Long targetId, String targetType);
    boolean isCollected(Long userId, Long targetId, String targetType);
}
package com.craft.service.impl;

import com.craft.entity.CollectionItem;
import com.craft.mapper.CollectionMapper;
import com.craft.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private CollectionMapper collectionMapper;

    @Override
    public List<CollectionItem> findAll() {
        return collectionMapper.findAll();
    }

    @Override
    public CollectionItem findById(Long id) {
        return collectionMapper.findById(id);
    }

    @Override
    public List<CollectionItem> findByUserId(Long userId) {
        return collectionMapper.findByUserId(userId);
    }

    @Override
    public CollectionItem findByUserIdAndTargetIdAndType(Long userId, Long targetId, String targetType) {
        return collectionMapper.findByUserIdAndTargetIdAndType(userId, targetId, targetType);
    }

    @Override
    public void insert(CollectionItem collection) {
        collectionMapper.insert(collection);
    }

    @Override
    public void delete(Long id) {
        collectionMapper.delete(id);
    }

    @Override
    public void deleteByUserIdAndTargetIdAndType(Long userId, Long targetId, String targetType) {
        collectionMapper.deleteByUserIdAndTargetIdAndType(userId, targetId, targetType);
    }

    @Override
    public boolean isCollected(Long userId, Long targetId, String targetType) {
        CollectionItem collection = collectionMapper.findByUserIdAndTargetIdAndType(userId, targetId, targetType);
        return collection != null;
    }
}
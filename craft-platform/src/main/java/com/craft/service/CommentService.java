package com.craft.service;

import com.craft.entity.Comment;
import java.util.List;

public interface CommentService {
    List<Comment> findAll();
    Comment findById(Long id);
    List<Comment> findByUserId(Long userId);
    List<Comment> findByTargetIdAndType(Long targetId, String targetType);
    List<Comment> findActiveComments();
    void insert(Comment comment);
    void update(Comment comment);
    void delete(Long id);
    void updateStatus(Long id, String status);
}
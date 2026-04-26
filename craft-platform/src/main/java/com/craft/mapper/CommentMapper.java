package com.craft.mapper;

import com.craft.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> findAll();
    Comment findById(Long id);
    List<Comment> findByUserId(Long userId);
    List<Comment> findByTargetIdAndType(Long targetId, String targetType);
    void insert(Comment comment);
    void update(Comment comment);
    void delete(Long id);
    List<Comment> findActiveComments();
}
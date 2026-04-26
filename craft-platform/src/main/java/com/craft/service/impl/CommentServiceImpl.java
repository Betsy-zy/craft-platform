package com.craft.service.impl;

import com.craft.entity.Comment;
import com.craft.mapper.CommentMapper;
import com.craft.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> findAll() {
        return commentMapper.findAll();
    }

    @Override
    public Comment findById(Long id) {
        return commentMapper.findById(id);
    }

    @Override
    public List<Comment> findByUserId(Long userId) {
        return commentMapper.findByUserId(userId);
    }

    @Override
    public List<Comment> findByTargetIdAndType(Long targetId, String targetType) {
        return commentMapper.findByTargetIdAndType(targetId, targetType);
    }

    @Override
    public List<Comment> findActiveComments() {
        return commentMapper.findActiveComments();
    }

    @Override
    public void insert(Comment comment) {
        comment.setStatus("pending");
        commentMapper.insert(comment);
    }

    @Override
    public void update(Comment comment) {
        commentMapper.update(comment);
    }

    @Override
    public void delete(Long id) {
        commentMapper.delete(id);
    }

    @Override
    public void updateStatus(Long id, String status) {
        Comment comment = commentMapper.findById(id);
        if (comment != null) {
            comment.setStatus(status);
            commentMapper.update(comment);
        }
    }
}
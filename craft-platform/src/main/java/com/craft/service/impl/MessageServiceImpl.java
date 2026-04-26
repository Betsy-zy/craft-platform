package com.craft.service.impl;

import com.craft.entity.Message;
import com.craft.mapper.MessageMapper;
import com.craft.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<Message> findAll() {
        return messageMapper.findAll();
    }

    @Override
    public List<Message> findByUserId(Long userId) {
        return messageMapper.findByUserId(userId);
    }

    @Override
    public List<Message> findUnreadByUserId(Long userId) {
        return messageMapper.findUnreadByUserId(userId);
    }

    @Override
    public int countUnreadByUserId(Long userId) {
        return messageMapper.countUnreadByUserId(userId);
    }

    @Override
    public Message findById(Long id) {
        return messageMapper.findById(id);
    }

    @Override
    public void insert(Message message) {
        messageMapper.insert(message);
    }

    @Override
    public void update(Message message) {
        messageMapper.update(message);
    }

    @Override
    public void markAsRead(Long id) {
        messageMapper.markAsRead(id);
    }

    @Override
    public void markAllAsRead(Long userId) {
        messageMapper.markAllAsRead(userId);
    }

    @Override
    public void delete(Long id) {
        messageMapper.delete(id);
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        messageMapper.deleteAllByUserId(userId);
    }
}
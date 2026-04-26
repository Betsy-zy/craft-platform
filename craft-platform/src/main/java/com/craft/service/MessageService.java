package com.craft.service;

import com.craft.entity.Message;
import java.util.List;

public interface MessageService {
    List<Message> findAll();
    List<Message> findByUserId(Long userId);
    List<Message> findUnreadByUserId(Long userId);
    int countUnreadByUserId(Long userId);
    Message findById(Long id);
    void insert(Message message);
    void update(Message message);
    void markAsRead(Long id);
    void markAllAsRead(Long userId);
    void delete(Long id);
    void deleteAllByUserId(Long userId);
}
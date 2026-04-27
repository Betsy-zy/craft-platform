package com.craft.mapper;

import com.craft.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MessageMapper {
    List<Message> findAll();
    List<Message> findByUserId(@Param("userId") Long userId);
    List<Message> findUnreadByUserId(@Param("userId") Long userId);
    int countUnreadByUserId(@Param("userId") Long userId);
    Message findById(@Param("id") Long id);
    void insert(Message message);
    void update(Message message);
    void markAsRead(@Param("id") Long id);
    void markAllAsRead(@Param("userId") Long userId);
    void delete(@Param("id") Long id);
    void deleteAllByUserId(@Param("userId") Long userId);
}
package com.craft.controller;

import com.craft.entity.Message;
import com.craft.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@CrossOrigin
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/user/{id}")
    public List<Message> findByUserId(@PathVariable Long id) {
        return messageService.findByUserId(id);
    }

    @GetMapping("/unread/{id}")
    public List<Message> findUnreadByUserId(@PathVariable Long id) {
        return messageService.findUnreadByUserId(id);
    }

    @GetMapping("/unread-count/{id}")
    public int countUnreadByUserId(@PathVariable Long id) {
        return messageService.countUnreadByUserId(id);
    }

    @GetMapping("/{id}")
    public Message findById(@PathVariable Long id) {
        return messageService.findById(id);
    }

    @PostMapping
    public void insert(@RequestBody Message message) {
        messageService.insert(message);
    }

    @PutMapping
    public void update(@RequestBody Message message) {
        messageService.update(message);
    }

    @PutMapping("/read/{id}")
    public void markAsRead(@PathVariable Long id) {
        messageService.markAsRead(id);
    }

    @PutMapping("/read-all/{id}")
    public void markAllAsRead(@PathVariable Long id) {
        messageService.markAllAsRead(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        messageService.delete(id);
    }

    @DeleteMapping("/all/{id}")
    public void deleteAll(@PathVariable Long id) {
        messageService.deleteAllByUserId(id);
    }
}
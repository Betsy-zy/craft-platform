package com.craft.controller;

import com.craft.entity.Comment;
import com.craft.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public List<Comment> findAll() {
        return commentService.findAll();
    }

    @GetMapping("/active")
    public List<Comment> findActiveComments() {
        return commentService.findActiveComments();
    }

    @GetMapping("/user/{id}")
    public List<Comment> findByUserId(@PathVariable Long id) {
        return commentService.findByUserId(id);
    }

    @GetMapping("/target")
    public List<Comment> findByTargetIdAndType(@RequestParam Long targetId, @RequestParam String targetType) {
        return commentService.findByTargetIdAndType(targetId, targetType);
    }

    @GetMapping("/{id}")
    public Comment findById(@PathVariable Long id) {
        return commentService.findById(id);
    }

    @PostMapping
    public void insert(@RequestBody Comment comment) {
        commentService.insert(comment);
    }

    @PutMapping
    public void update(@RequestBody Comment comment) {
        commentService.update(comment);
    }

    @PutMapping("/status/{id}")
    public void updateStatus(@PathVariable Long id, @RequestParam String status) {
        commentService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }
}
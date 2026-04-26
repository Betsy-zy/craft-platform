package com.craft.controller;

import com.craft.entity.CollectionItem;
import com.craft.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collections")
@CrossOrigin
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @GetMapping
    public List<CollectionItem> findAll() {
        return collectionService.findAll();
    }

    @GetMapping("/user/{id}")
    public List<CollectionItem> findByUserId(@PathVariable Long id) {
        return collectionService.findByUserId(id);
    }

    @GetMapping("/check")
    public boolean isCollected(@RequestParam Long userId, @RequestParam Long targetId, @RequestParam String targetType) {
        return collectionService.isCollected(userId, targetId, targetType);
    }

    @GetMapping("/{id}")
    public CollectionItem findById(@PathVariable Long id) {
        return collectionService.findById(id);
    }

    @PostMapping
    public void insert(@RequestBody CollectionItem collection) {
        collectionService.insert(collection);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        collectionService.delete(id);
    }

    @DeleteMapping("/user-target")
    public void deleteByUserIdAndTargetIdAndType(@RequestParam Long userId, @RequestParam Long targetId, @RequestParam String targetType) {
        collectionService.deleteByUserIdAndTargetIdAndType(userId, targetId, targetType);
    }
}
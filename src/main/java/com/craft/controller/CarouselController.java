package com.craft.controller;

import com.craft.entity.Carousel;
import com.craft.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carousels")
@CrossOrigin
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    @GetMapping
    public List<Carousel> findAll() {
        return carouselService.findAll();
    }

    @GetMapping("/active")
    public List<Carousel> findActiveCarousels() {
        return carouselService.findActiveCarousels();
    }

    @GetMapping("/{id}")
    public Carousel findById(@PathVariable Long id) {
        return carouselService.findById(id);
    }

    @PostMapping
    public void insert(@RequestBody Carousel carousel) {
        carouselService.insert(carousel);
    }

    @PutMapping
    public void update(@RequestBody Carousel carousel) {
        carouselService.update(carousel);
    }

    @PutMapping("/status/{id}")
    public void updateStatus(@PathVariable Long id, @RequestParam String status) {
        carouselService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        carouselService.delete(id);
    }
}
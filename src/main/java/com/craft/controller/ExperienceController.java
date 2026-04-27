package com.craft.controller;

import com.craft.entity.Experience;
import com.craft.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/experiences")
@CrossOrigin
public class ExperienceController {
    @Autowired
    private ExperienceService experienceService;

    @PostMapping
    public Experience create(@RequestBody Experience experience) {
        return experienceService.create(experience);
    }

    @GetMapping
    public List<Experience> findAll() {
        return experienceService.findAll();
    }

    @GetMapping("/active")
    public List<Experience> findActiveExperiences() {
        return experienceService.findActiveExperiences();
    }

    @GetMapping("/{id}")
    public Experience findById(@PathVariable Long id) {
        return experienceService.findById(id);
    }

    @GetMapping("/craftsman/{craftsmanId}")
    public List<Experience> findByCraftsmanId(@PathVariable Long craftsmanId) {
        return experienceService.findByCraftsmanId(craftsmanId);
    }

    @PutMapping
    public Experience update(@RequestBody Experience experience) {
        return experienceService.update(experience);
    }

    @PutMapping("/status/{id}")
    public void updateStatus(@PathVariable Long id, @RequestParam String status) {
        experienceService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        experienceService.delete(id);
    }
}
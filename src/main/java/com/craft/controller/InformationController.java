package com.craft.controller;

import com.craft.entity.Information;
import com.craft.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/information")
@CrossOrigin
public class InformationController {
    @Autowired
    private InformationService informationService;

    @PostMapping
    public Information create(@RequestBody Information information) {
        return informationService.create(information);
    }

    @GetMapping
    public List<Information> findAll() {
        return informationService.findAll();
    }

    @GetMapping("/{id}")
    public Information findById(@PathVariable Long id) {
        return informationService.findById(id);
    }

    @PutMapping
    public Information update(@RequestBody Information information) {
        return informationService.update(information);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        informationService.delete(id);
    }

    @PutMapping("/status/{id}")
    public Information updateStatus(@PathVariable Long id, @RequestBody Information information) {
        return informationService.updateStatus(id, information.getStatus());
    }
}
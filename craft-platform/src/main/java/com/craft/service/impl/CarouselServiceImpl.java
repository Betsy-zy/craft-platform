package com.craft.service.impl;

import com.craft.entity.Carousel;
import com.craft.mapper.CarouselMapper;
import com.craft.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public List<Carousel> findAll() {
        return carouselMapper.findAll();
    }

    @Override
    public Carousel findById(Long id) {
        return carouselMapper.findById(id);
    }

    @Override
    public List<Carousel> findActiveCarousels() {
        return carouselMapper.findActiveCarousels();
    }

    @Override
    public void insert(Carousel carousel) {
        carousel.setStatus("active");
        carouselMapper.insert(carousel);
    }

    @Override
    public void update(Carousel carousel) {
        carouselMapper.update(carousel);
    }

    @Override
    public void delete(Long id) {
        carouselMapper.delete(id);
    }

    @Override
    public void updateStatus(Long id, String status) {
        Carousel carousel = carouselMapper.findById(id);
        if (carousel != null) {
            carousel.setStatus(status);
            carouselMapper.update(carousel);
        }
    }
}
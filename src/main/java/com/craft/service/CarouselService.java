package com.craft.service;

import com.craft.entity.Carousel;
import java.util.List;

public interface CarouselService {
    List<Carousel> findAll();
    Carousel findById(Long id);
    List<Carousel> findActiveCarousels();
    void insert(Carousel carousel);
    void update(Carousel carousel);
    void delete(Long id);
    void updateStatus(Long id, String status);
}
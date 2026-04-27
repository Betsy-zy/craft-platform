package com.craft.mapper;

import com.craft.entity.Carousel;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CarouselMapper {
    List<Carousel> findAll();
    Carousel findById(Long id);
    List<Carousel> findActiveCarousels();
    void insert(Carousel carousel);
    void update(Carousel carousel);
    void delete(Long id);
}
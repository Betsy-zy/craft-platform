package com.craft.mapper;

import com.craft.entity.ProductOrder;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ProductOrderMapper {
    List<ProductOrder> findAll();
    ProductOrder findById(Long id);
    List<ProductOrder> findByUserId(Long userId);
    List<ProductOrder> findByWorkId(Long workId);
    void insert(ProductOrder productOrder);
    void update(ProductOrder productOrder);
    void delete(Long id);
}
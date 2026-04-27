package com.craft.service;

import com.craft.entity.ProductOrder;
import com.craft.entity.ProductOrderDTO;
import java.util.List;

public interface ProductOrderService {
    List<ProductOrder> findAll();
    List<ProductOrderDTO> findAllWithDetails();
    ProductOrder findById(Long id);
    List<ProductOrder> findByUserId(Long userId);
    List<ProductOrder> findByWorkId(Long workId);
    void insert(ProductOrder productOrder);
    void update(ProductOrder productOrder);
    void delete(Long id);
    void updateStatus(Long id, String status);
}
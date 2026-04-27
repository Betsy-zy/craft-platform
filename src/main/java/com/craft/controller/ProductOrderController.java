package com.craft.controller;

import com.craft.entity.ProductOrder;
import com.craft.entity.ProductOrderDTO;
import com.craft.service.ProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-orders")
@CrossOrigin
public class ProductOrderController {

    @Autowired
    private ProductOrderService productOrderService;

    @GetMapping
    public List<ProductOrder> findAll() {
        return productOrderService.findAll();
    }

    @GetMapping("/details")
    public List<ProductOrderDTO> findAllWithDetails() {
        return productOrderService.findAllWithDetails();
    }

    @GetMapping("/user/{id}")
    public List<ProductOrder> findByUserId(@PathVariable Long id) {
        return productOrderService.findByUserId(id);
    }

    @GetMapping("/work/{id}")
    public List<ProductOrder> findByWorkId(@PathVariable Long id) {
        return productOrderService.findByWorkId(id);
    }

    @GetMapping("/{id}")
    public ProductOrder findById(@PathVariable Long id) {
        return productOrderService.findById(id);
    }

    @PostMapping
    public void insert(@RequestBody ProductOrder productOrder) {
        productOrderService.insert(productOrder);
    }

    @PutMapping
    public void update(@RequestBody ProductOrder productOrder) {
        productOrderService.update(productOrder);
    }

    @PutMapping("/status/{id}")
    public void updateStatus(@PathVariable Long id, @RequestParam String status) {
        productOrderService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productOrderService.delete(id);
    }
}
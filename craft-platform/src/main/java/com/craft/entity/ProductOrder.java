package com.craft.entity;

import java.util.Date;
import lombok.Data;

@Data
public class ProductOrder {
    private Long id;
    private Long userId;
    private Long workId;
    private Integer quantity;
    private Double totalPrice;
    private String status;
    private Date createTime;
    private Date cancelTime;
}
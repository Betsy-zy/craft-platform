package com.craft.entity;

import java.util.Date;
import lombok.Data;

@Data
public class ProductOrderDTO {
    private Long id;
    private Long userId;
    private String username;
    private Long workId;
    private String workName;
    private Integer quantity;
    private Double totalPrice;
    private String status;
    private Date createTime;
    private Date cancelTime;
}
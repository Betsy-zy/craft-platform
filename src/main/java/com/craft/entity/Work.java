package com.craft.entity;

import java.util.Date;
import lombok.Data;

@Data
public class Work {
    private Long id;
    private String name;
    private String title;
    private String description;
    private String image;
    private Double price;
    private Integer stock;
    private Long craftsmanId;
    private String status;
    private Date createTime;
}
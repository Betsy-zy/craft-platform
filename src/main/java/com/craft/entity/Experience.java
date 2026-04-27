package com.craft.entity;

import java.util.Date;
import lombok.Data;

@Data
public class Experience {
    private Long id;
    private String name;
    private String title;
    private String description;
    private Double price;
    private String duration;
    private Long craftsmanId;
    private String status;
    private Date createTime;
}
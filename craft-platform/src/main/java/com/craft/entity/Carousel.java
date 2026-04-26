package com.craft.entity;

import java.util.Date;
import lombok.Data;

@Data
public class Carousel {
    private Long id;
    private String image;
    private String title;
    private String link;
    private Integer orderNum;
    private String status;
    private Date createTime;
}
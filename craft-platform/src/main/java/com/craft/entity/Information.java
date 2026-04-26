package com.craft.entity;

import java.util.Date;
import lombok.Data;

@Data
public class Information {
    private Long id;
    private String title;
    private String content;
    private String image;
    private String status;
    private Date createTime;
}
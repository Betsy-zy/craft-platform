package com.craft.entity;

import java.util.Date;
import lombok.Data;

@Data
public class Craftsman {
    private Long id;
    private String name;
    private String title;
    private String description;
    private String avatar;
    private String contact;
    private String status;
    private Long userId;
    private Date createTime;
}
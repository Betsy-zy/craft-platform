package com.craft.entity;

import java.util.Date;
import lombok.Data;

@Data
public class Comment {
    private Long id;
    private Long userId;
    private Long targetId;
    private String targetType;
    private String content;
    private String status;
    private Date createTime;
}
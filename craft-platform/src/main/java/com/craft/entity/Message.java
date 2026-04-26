package com.craft.entity;

import java.util.Date;
import lombok.Data;

@Data
public class Message {
    private Long id;
    private Long userId;
    private String content;
    private String type;
    private String status;
    private Date createTime;
}
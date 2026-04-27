package com.craft.entity;

import java.util.Date;
import lombok.Data;

@Data
public class CollectionItem {
    private Long id;
    private Long userId;
    private Long targetId;
    private String targetType;
    private Date createTime;
}
package com.craft.entity;

import java.util.Date;
import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String role;
    private String status;
    private Date createTime;
    private String avatar;
}
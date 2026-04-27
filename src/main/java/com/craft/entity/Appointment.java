package com.craft.entity;

import java.util.Date;
import lombok.Data;

@Data
public class Appointment {
    private Long id;
    private Long userId;
    private Long experienceId;
    private Date appointmentTime;
    private String status;
    private Date createTime;
}
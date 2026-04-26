package com.craft.mapper;

import com.craft.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AppointmentMapper {
    int insert(Appointment appointment);
    Appointment findById(Long id);
    List<Appointment> findAll();
    List<Appointment> findByUserId(Long userId);
    List<Appointment> findByStatus(String status);
    List<Appointment> findByExperienceId(Long experienceId);
    int update(Appointment appointment);
    int deleteById(Long id);
}
package com.craft.service;

import com.craft.entity.Appointment;
import com.craft.entity.Message;
import com.craft.mapper.AppointmentMapper;
import com.craft.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private MessageMapper messageMapper;

    public Appointment create(Appointment appointment) {
        appointment.setCreateTime(new Date());
        appointment.setStatus("pending");
        appointmentMapper.insert(appointment);

        // 生成预约创建消息给用户
        if (appointment.getUserId() != null && messageMapper != null) {
            Message userMessage = new Message();
            userMessage.setUserId(appointment.getUserId());
            userMessage.setContent("您的预约申请已提交，商家将尽快处理。");
            userMessage.setType("appointment");
            userMessage.setStatus("unread");
            userMessage.setCreateTime(new Date());
            messageMapper.insert(userMessage);
        }

        // 生成预约创建消息给管理员
        if (messageMapper != null) {
            Message adminMessage = new Message();
            adminMessage.setUserId(1L); // 假设管理员的用户ID为1
            adminMessage.setContent("【商家通知】新的预约申请已提交，请及时处理。");
            adminMessage.setType("appointment");
            adminMessage.setStatus("unread");
            adminMessage.setCreateTime(new Date());
            messageMapper.insert(adminMessage);
        }

        return appointment;
    }

    public List<Appointment> findAll() {
        return appointmentMapper.findAll();
    }

    public List<Appointment> findByUserId(Long userId) {
        return appointmentMapper.findByUserId(userId);
    }

    public List<Appointment> findByStatus(String status) {
        return appointmentMapper.findByStatus(status);
    }

    public List<Appointment> findByExperienceId(Long experienceId) {
        return appointmentMapper.findByExperienceId(experienceId);
    }

    public Appointment update(Appointment appointment) {
        appointmentMapper.update(appointment);
        return appointment;
    }

    public void delete(Long id) {
        appointmentMapper.deleteById(id);
    }

    public Appointment updateStatus(Long id, String status) {
        Appointment appointment = appointmentMapper.findById(id);
        if (appointment != null) {
            appointment.setStatus(status);
            appointmentMapper.update(appointment);

            // 生成预约状态更新消息给用户
            if (appointment.getUserId() != null && messageMapper != null) {
                Message userMessage = new Message();
                userMessage.setUserId(appointment.getUserId());

                if ("accepted".equals(status)) {
                    userMessage.setContent("您的预约申请已被商家接受，请注意查看详情。");
                } else if ("rejected".equals(status)) {
                    userMessage.setContent("您的预约申请已被商家拒绝，请选择其他时间或项目。");
                } else if ("completed".equals(status)) {
                    userMessage.setContent("您的预约已完成，感谢您的参与。");
                }

                userMessage.setType("appointment");
                userMessage.setStatus("unread");
                userMessage.setCreateTime(new Date());
                messageMapper.insert(userMessage);
            }
        }
        return appointment;
    }
}
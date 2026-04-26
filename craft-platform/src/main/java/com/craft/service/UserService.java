package com.craft.service;

import com.craft.entity.Appointment;
import com.craft.entity.Craftsman;
import com.craft.entity.Experience;
import com.craft.entity.ProductOrder;
import com.craft.entity.User;
import com.craft.entity.Work;
import com.craft.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User register(User user) {
        user.setCreateTime(new Date());
        user.setRole("user");
        user.setStatus("active");
        userMapper.insert(user);
        return user;
    }

    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // 检查用户状态是否为活跃
            if (!"active".equals(user.getStatus())) {
                return null;
            }
            // 直接返回数据库中存储的用户信息，不再根据用户名强制分配角色
            return user;
        }
        return null;
    }

    public User findById(Long id) {
        return userMapper.findById(id);
    }

    public User update(User user) {
        userMapper.update(user);
        return user;
    }

    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Autowired
    private CraftsmanService craftsmanService;
    @Autowired
    private ProductOrderService productOrderService;
    @Autowired
    private ExperienceService experienceService;
    @Autowired
    private WorkService workService;
    @Autowired
    private AppointmentService appointmentService;

    public void delete(Long id) {
        // 先删除该用户在商品订单表中的对应记录（作为买家）
        List<ProductOrder> productOrders = productOrderService.findByUserId(id);
        for (ProductOrder order : productOrders) {
            productOrderService.delete(order.getId());
        }
        // 再删除该用户在匠人表中的对应记录
        Craftsman craftsman = craftsmanService.findByUserId(id);
        if (craftsman != null) {
            // 先删除该匠人在体验项目表中的对应记录
            List<Experience> experiences = experienceService.findByCraftsmanId(craftsman.getId());
            for (Experience experience : experiences) {
                // 先删除与该体验项目相关的所有预约
                List<Appointment> appointments = appointmentService.findByExperienceId(experience.getId());
                for (Appointment appointment : appointments) {
                    appointmentService.delete(appointment.getId());
                }
                // 再删除体验项目
                experienceService.delete(experience.getId());
            }
            // 再删除该匠人在作品表中的对应记录
            List<Work> works = workService.findByCraftsmanId(craftsman.getId());
            for (Work work : works) {
                workService.delete(work.getId());
            }

            // 最后删除匠人
            craftsmanService.delete(craftsman.getId());
        }
        // 最后删除用户
        userMapper.deleteById(id);
    }
}
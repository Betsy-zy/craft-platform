package com.craft.service.impl;

import com.craft.entity.ProductOrder;
import com.craft.entity.ProductOrderDTO;
import com.craft.entity.Message;
import com.craft.entity.Work;
import com.craft.entity.User;
import com.craft.mapper.ProductOrderMapper;
import com.craft.mapper.MessageMapper;
import com.craft.mapper.WorkMapper;
import com.craft.mapper.UserMapper;
import com.craft.service.ProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    @Autowired
    private ProductOrderMapper productOrderMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<ProductOrder> findAll() {
        if (productOrderMapper == null) {
            return null;
        }
        return productOrderMapper.findAll();
    }

    @Override
    public List<ProductOrderDTO> findAllWithDetails() {
        if (productOrderMapper == null) {
            return null;
        }
        List<ProductOrder> orders = productOrderMapper.findAll();
        List<ProductOrderDTO> dtos = new ArrayList<>();
        for (ProductOrder order : orders) {
            ProductOrderDTO dto = new ProductOrderDTO();
            dto.setId(order.getId());
            dto.setUserId(order.getUserId());
            dto.setWorkId(order.getWorkId());
            dto.setQuantity(order.getQuantity());
            dto.setTotalPrice(order.getTotalPrice());
            dto.setStatus(order.getStatus());
            dto.setCreateTime(order.getCreateTime());
            dto.setCancelTime(order.getCancelTime());

            // 获取用户名称
            if (order.getUserId() != null && userMapper != null) {
                User user = userMapper.findById(order.getUserId());
                if (user != null) {
                    dto.setUsername(user.getUsername());
                }
            }

            // 获取商品名称
            if (order.getWorkId() != null && workMapper != null) {
                Work work = workMapper.findById(order.getWorkId());
                if (work != null) {
                    dto.setWorkName(work.getName());
                }
            }

            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public ProductOrder findById(Long id) {
        if (productOrderMapper == null) {
            return null;
        }
        return productOrderMapper.findById(id);
    }

    @Override
    public List<ProductOrder> findByUserId(Long userId) {
        if (productOrderMapper == null) {
            return null;
        }
        return productOrderMapper.findByUserId(userId);
    }

    @Override
    public List<ProductOrder> findByWorkId(Long workId) {
        if (productOrderMapper == null) {
            return null;
        }
        return productOrderMapper.findByWorkId(workId);
    }

    @Override
    @Transactional
    public void insert(ProductOrder productOrder) {
        if (productOrder == null || productOrderMapper == null) {
            throw new RuntimeException("订单信息不能为空");
        }

        // 验证work_id是否存在
        if (productOrder.getWorkId() == null || workMapper == null) {
            throw new RuntimeException("商品信息无效");
        }

        Work work = workMapper.findById(productOrder.getWorkId());
        if (work == null) {
            throw new RuntimeException("商品不存在");
        }

        // 检查库存是否足够
        if (work.getStock() < productOrder.getQuantity()) {
            throw new RuntimeException("库存不足，无法创建订单");
        }

        // 减少库存
        work.setStock(work.getStock() - productOrder.getQuantity());

        // 如果库存为0，自动下架
        if (work.getStock() == 0) {
            work.setStatus("inactive");
        }

        // 更新作品信息
        workMapper.update(work);

        productOrder.setStatus("pending");
        productOrderMapper.insert(productOrder);

        // 生成订单创建消息给用户
        if (productOrder.getUserId() != null && messageMapper != null) {
            Message userMessage = new Message();
            userMessage.setUserId(productOrder.getUserId());
            userMessage.setContent("您的作品订单已成功支付，商家正在准备发货。");
            userMessage.setType("order");
            userMessage.setStatus("unread");
            userMessage.setCreateTime(new Date());
            messageMapper.insert(userMessage);
        }

        // 生成订单创建消息给管理员
        if (messageMapper != null) {
            Message adminMessage = new Message();
            adminMessage.setUserId(1L);
            String userName = "";
            if (productOrder.getUserId() != null && userMapper != null) {
                User user = userMapper.findById(productOrder.getUserId());
                if (user != null) {
                    userName = user.getUsername();
                } else {
                    userName = productOrder.getUserId().toString();
                }
            }
            adminMessage.setContent("【商家通知】新订单 " + (productOrder.getId() != null ? productOrder.getId() : "") + "：用户 " + userName + " 购买了作品 '" + work.getName() + "'，数量 " + productOrder.getQuantity() + "，总价 ¥" + productOrder.getTotalPrice() + "，请及时处理发货。");
            adminMessage.setType("order");
            adminMessage.setStatus("unread");
            adminMessage.setCreateTime(new Date());
            messageMapper.insert(adminMessage);
        }
    }

    @Override
    public void update(ProductOrder productOrder) {
        if (productOrder == null || productOrderMapper == null) {
            return;
        }
        productOrderMapper.update(productOrder);
    }

    @Override
    public void delete(Long id) {
        if (id == null || productOrderMapper == null) {
            return;
        }
        productOrderMapper.delete(id);
    }

    @Override
    public void updateStatus(Long id, String status) {
        if (id == null || productOrderMapper == null) {
            return;
        }

        ProductOrder productOrder = productOrderMapper.findById(id);
        if (productOrder != null) {
            String oldStatus = productOrder.getStatus();

            productOrder.setStatus(status);

            if ("cancelled".equals(status)) {
                productOrder.setCancelTime(new Date());
            }

            productOrderMapper.update(productOrder);

            if ("cancelled".equals(status) && ("pending".equals(oldStatus) || "paid".equals(oldStatus))) {
                if (workMapper != null && productOrder.getWorkId() != null) {
                    Work work = workMapper.findById(productOrder.getWorkId());
                    if (work != null) {
                        work.setStock(work.getStock() + productOrder.getQuantity());
                        if (work.getStock() > 0) {
                            work.setStatus("active");
                        }
                        workMapper.update(work);
                    }
                }
            }

            if (productOrder.getUserId() != null && messageMapper != null) {
                Message userMessage = new Message();
                userMessage.setUserId(productOrder.getUserId());

                if ("shipped".equals(status)) {
                    userMessage.setContent("您的作品订单已发货，请注意查收。");
                } else if ("completed".equals(status)) {
                    userMessage.setContent("您的作品订单已完成，感谢您的购买。");
                } else if ("cancelled".equals(status)) {
                    userMessage.setContent("您的作品订单已取消。");
                }

                userMessage.setType("order");
                userMessage.setStatus("unread");
                userMessage.setCreateTime(new Date());
                messageMapper.insert(userMessage);
            }
        }
    }
}
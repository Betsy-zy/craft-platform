package com.craft.service;

import com.craft.entity.Craftsman;
import com.craft.mapper.CraftsmanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CraftsmanService {
    @Autowired
    private CraftsmanMapper craftsmanMapper;

    public Craftsman create(Craftsman craftsman) {
        craftsman.setCreateTime(new Date());
        if (craftsman.getStatus() == null || craftsman.getStatus().isEmpty()) {
            craftsman.setStatus("active");
        }
        craftsmanMapper.insert(craftsman);
        return craftsman;
    }

    public List<Craftsman> findAll() {
        return craftsmanMapper.findAll();
    }

    public List<Craftsman> findByStatus(String status) {
        return craftsmanMapper.findByStatus(status);
    }

    public Craftsman findByUserId(Long userId) {
        return craftsmanMapper.findByUserId(userId);
    }

    public Craftsman findById(Long id) {
        return craftsmanMapper.findById(id);
    }

    public Craftsman update(Craftsman craftsman) {
        craftsmanMapper.update(craftsman);
        return craftsman;
    }

    public void updateStatus(Long id, String status) {
        Craftsman craftsman = craftsmanMapper.findById(id);
        if (craftsman != null) {
            craftsman.setStatus(status);
            craftsmanMapper.update(craftsman);
        }
    }

    public void delete(Long id) {
        craftsmanMapper.deleteById(id);
    }
}
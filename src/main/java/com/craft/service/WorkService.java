package com.craft.service;

import com.craft.entity.Work;
import com.craft.mapper.WorkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WorkService {
    @Autowired
    private WorkMapper workMapper;

    public Work create(Work work) {
        work.setCreateTime(new Date());
        if (work.getStatus() == null) {
            work.setStatus("active");
        }
        if (work.getStock() == null) {
            work.setStock(0);
        }
        workMapper.insert(work);
        return work;
    }

    public List<Work> findAll() {
        return workMapper.findAll();
    }

    public List<Work> findActiveWorks() {
        return workMapper.findActiveWorks();
    }

    public Work findById(Long id) {
        return workMapper.findById(id);
    }

    public List<Work> findByCraftsmanId(Long craftsmanId) {
        return workMapper.findByCraftsmanId(craftsmanId);
    }

    public Work update(Work work) {
        workMapper.update(work);
        return work;
    }

    public void updateStatus(Long id, String status) {
        Work work = workMapper.findById(id);
        if (work != null) {
            work.setStatus(status);
            workMapper.update(work);
        }
    }

    public void delete(Long id) {
        workMapper.deleteById(id);
    }
}
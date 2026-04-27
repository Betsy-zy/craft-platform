package com.craft.service;

import com.craft.entity.Information;
import com.craft.mapper.InformationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InformationService {
    @Autowired
    private InformationMapper informationMapper;

    public Information create(Information information) {
        information.setCreateTime(new Date());
        informationMapper.insert(information);
        return information;
    }

    public List<Information> findAll() {
        return informationMapper.findAll();
    }

    public Information findById(Long id) {
        return informationMapper.findById(id);
    }

    public Information update(Information information) {
        informationMapper.update(information);
        return information;
    }

    public void delete(Long id) {
        informationMapper.deleteById(id);
    }

    public Information updateStatus(Long id, String status) {
        Information information = informationMapper.findById(id);
        if (information != null) {
            information.setStatus(status);
            informationMapper.update(information);
        }
        return information;
    }
}
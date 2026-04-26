package com.craft.controller;

import com.craft.entity.Work;
import com.craft.service.WorkService;
import com.craft.utils.AliyunOSSOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/works")
@CrossOrigin
public class WorkController {
    @Autowired
    private WorkService workService;

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    @PostMapping
    public Work create(@RequestBody Work work) {
        return workService.create(work);
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println("开始上传文件");
        
        if (file.isEmpty()) {
            System.out.println("文件为空");
            return "文件为空";
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        System.out.println("原始文件名: " + originalFilename);

        // 上传文件到阿里云OSS
        System.out.println("开始上传文件到阿里云OSS");
        byte[] content;
        try {
            content = file.getBytes();
        } catch (Exception e) {
            System.out.println("获取文件内容失败");
            e.printStackTrace();
            return "获取文件内容失败";
        }
        String ossUrl = aliyunOSSOperator.upload(content, originalFilename);
        System.out.println("文件上传到阿里云OSS完成，URL: " + ossUrl);
        // 返回阿里云OSS的URL
        return ossUrl;
    }

    @GetMapping
    public List<Work> findAll() {
        return workService.findAll();
    }

    @GetMapping("/active")
    public List<Work> findActiveWorks() {
        return workService.findActiveWorks();
    }

    @GetMapping("/{id}")
    public Work findById(@PathVariable Long id) {
        return workService.findById(id);
    }

    @GetMapping("/craftsman/{craftsmanId}")
    public List<Work> findByCraftsmanId(@PathVariable Long craftsmanId) {
        return workService.findByCraftsmanId(craftsmanId);
    }

    @PutMapping
    public Work update(@RequestBody Work work) {
        return workService.update(work);
    }

    @PutMapping("/status/{id}")
    public void updateStatus(@PathVariable Long id, @RequestParam String status) {
        workService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        workService.delete(id);
    }
}
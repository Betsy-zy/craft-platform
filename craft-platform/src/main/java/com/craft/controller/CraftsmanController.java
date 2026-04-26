package com.craft.controller;

import com.craft.entity.Craftsman;
import com.craft.service.CraftsmanService;
import com.craft.utils.AliyunOSSOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/craftsmen")
@CrossOrigin
public class CraftsmanController {
    @Autowired
    private CraftsmanService craftsmanService;

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    @PostMapping
    public Craftsman create(@RequestBody Craftsman craftsman) {
        return craftsmanService.create(craftsman);
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
    public List<Craftsman> findAll() {
        return craftsmanService.findAll();
    }

    @GetMapping("/status/{status}")
    public List<Craftsman> findByStatus(@PathVariable String status) {
        return craftsmanService.findByStatus(status);
    }

    @GetMapping("/user/{userId}")
    public Craftsman findByUserId(@PathVariable Long userId) {
        return craftsmanService.findByUserId(userId);
    }

    @GetMapping("/{id}")
    public Craftsman findById(@PathVariable Long id) {
        return craftsmanService.findById(id);
    }

    @PutMapping
    public Craftsman update(@RequestBody Craftsman craftsman) {
        return craftsmanService.update(craftsman);
    }

    @PutMapping("/status/{id}")
    public void updateStatus(@PathVariable Long id, @RequestParam String status) {
        craftsmanService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        craftsmanService.delete(id);
    }
}
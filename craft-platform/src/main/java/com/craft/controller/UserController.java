package com.craft.controller;

import com.craft.entity.User;
import com.craft.service.UserService;
import com.craft.utils.AliyunOSSOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return userService.login(user.getUsername(), user.getPassword());
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping
    public java.util.List<User> findAll() {
        return userService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
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
}
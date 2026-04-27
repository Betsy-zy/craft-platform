package com.craft.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.craft.utils.AliyunOSSProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class AliyunOSSOperator {

    @Autowired
    private AliyunOSSProperties aliyunOSSProperties;

    public String upload(byte[] content, String originalFilename) {
        String endpoint = aliyunOSSProperties.getEndpoint();
        String bucketName = aliyunOSSProperties.getBucketName();
        String region = aliyunOSSProperties.getRegion();

        try {
            // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
            EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

            // 获取当前系统日期的字符串,格式为 yyyy/MM
            String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
            // 生成一个新的不重复的文件名
            String fileExtension = "";
            int dotIndex = originalFilename.lastIndexOf(".");
            if (dotIndex > 0) {
                fileExtension = originalFilename.substring(dotIndex);
            }
            String newFileName = UUID.randomUUID() + fileExtension;
            String objectName = dir + "/" + newFileName;

            // 创建OSSClient实例。
            ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
            clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
            OSS ossClient = OSSClientBuilder.create()
                    .endpoint(endpoint)
                    .credentialsProvider(credentialsProvider)
                    .clientConfiguration(clientBuilderConfiguration)
                    .region(region)
                    .build();

            try {
                ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content));
            } finally {
                ossClient.shutdown();
            }

            // 构建完整的OSS访问URL
            String protocol = endpoint.split("//")[0];
            String domain = endpoint.split("//")[1];
            String ossUrl = protocol + "//" + bucketName + "." + domain + "/" + objectName;
            System.out.println("生成的OSS URL: " + ossUrl);
            return ossUrl;
        } catch (Exception e) {
            System.out.println("文件上传到阿里云OSS失败:");
            e.printStackTrace();
            // 返回一个模拟的URL，以便前端能够正常显示
            return "https://example.com/default-image.jpg";
        }
    }

}
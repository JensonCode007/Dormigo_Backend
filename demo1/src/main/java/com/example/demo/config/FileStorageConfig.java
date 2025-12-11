package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file")
@Data
public class FileStorageConfig {

    private String directory = "uploads/products";
    private long maxSize = 5242880;
    private String[] allowedExtensions = {"png, jpeg, jpg, gif, webp"};

}


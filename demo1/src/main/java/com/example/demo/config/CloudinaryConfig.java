package com.example.demo.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Slf4j
public class CloudinaryConfig {
    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {


        // Check for null/empty values
        if (cloudName == null || cloudName.trim().isEmpty()) {
            throw new IllegalStateException("Cloudinary cloud-name is not configured!");
        }
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("Cloudinary api-key is not configured!");
        }
        if (apiSecret == null || apiSecret.trim().isEmpty()) {
            throw new IllegalStateException("Cloudinary api-secret is not configured!");
        }

        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName. trim(),
                "api_key", apiKey.trim(),
                "api_secret", apiSecret. trim(),
                "secure", true
        ));

        log.info("✅ Cloudinary bean created successfully");
        return cloudinary;
    }
}

package com.example.demo.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.folder}")
    private String folder;

    public Map<String, Object> uploadImage(MultipartFile file) {
        try{
            log.info("Uploading image to Cloudinary : {}", file.getOriginalFilename());
            log.info("========================================");
            log.info("📤 CLOUDINARY UPLOAD DEBUG");
            log.info("File name: {}", file.getOriginalFilename());
            log.info("File size: {} bytes", file.getSize());
            log.info("Content type: {}", file.getContentType());
            log.info("Is empty: {}", file.isEmpty());
            log.info("Folder: {}", folder);
            log.info("========================================");
            if(file.isEmpty()){
                throw new FileStorageException("File is empty");
            }
            String publicId = UUID.randomUUID().toString();
            Map<String, Object> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "public_id", publicId,
                            "resource_type", "image"
                    )
            );
            return result;
        }

        catch (IOException e){
            log.error("❌ CLOUDINARY UPLOAD FAILED");
            log.error("Error message: {}", e.getMessage());
            log.error("Error class: {}", e.getClass().getName());
            log.error("Stack trace:", e);
            log.error("========================================");
            throw new FileStorageException("Error while uploading image to Cloudinary");
        }
    }

    public void deleteImage(String publicId) {
        try {
            log.info("Deleting image from Cloudinary : {}", publicId);

            Map<String, Object> deleteImage = cloudinary.uploader().destroy(
                    folder + "/" + publicId,
                    ObjectUtils.emptyMap()
            );
            log.info("Successfully deleted image from Cloudinary : {}", publicId);
        }
        catch (IOException e){
            log.error("Error while deleting image from Cloudinary : {}", e.getMessage());
            throw new FileStorageException("Error while deleting image from Cloudinary");
        }
    }

    public String getOptimizedUrl(String publicId, Integer width, Integer height) {
        return cloudinary.url()
                .publicId(publicId)
                .transformation(new com.cloudinary.Transformation()
                        .width(width)
                        .height(height)
                        .crop("limit")
                        .quality("auto")
                        .fetchFormat("auto")
                )
                .secure(true)
                .generate();

    }
}

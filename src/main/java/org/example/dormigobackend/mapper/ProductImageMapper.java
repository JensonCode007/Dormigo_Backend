package org.example.dormigobackend.mapper;

import org.example.dormigobackend.Entity.ProductImage;
import org.example.dormigobackend.dto.response.ProductImageResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductImageMapper {
    public static ProductImageResponse toResponse(ProductImage productImage) {
        if(productImage==null){
            return null;
        }
        else{
            return ProductImageResponse.builder()
                    .imageId(productImage.getId())
                    .fileName(productImage.getFileName())
                    .fileSize(productImage.getFileSize())
                    .imageUrl(productImage.getImageUrl())
                    .uploadedAt(productImage.getUploadedAt())
                    .fileType(productImage.getFileType())
                    .build();
        }
    }
}

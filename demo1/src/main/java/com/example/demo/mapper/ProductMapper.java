package com.example.demo.mapper;


import com.example.demo.Entity.Product;
import com.example.demo.Entity.ProductImage;
import com.example.demo.Repository.ProductImageRepository;
import com.example.demo.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    @Autowired
    private ProductImageRepository productImageRepository;

    public static ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        } else {



            return ProductResponse.builder()
                    .id(product.getId())
                    .categoryId(product.getCategory().getId())
                    .categoryName(product.getCategory().getName())
                    .price(product.getPrice())
                    .description(product.getDescription())
                    .quantity(product.getQuantity())
                    .isAvailable(product.getIsAvailable())
                    .createdAt(product.getCreatedAt())
                    .title(product.getTitle())
                    .productImages(product.getProductImages().stream().map(ProductImage::getImageUrl).toList())
                    .condition(product.getProductCondition().name())
                    .seller(mapSellerInfo(product))
                    .build();
        }
    }
    private static ProductResponse.SellerInfo mapSellerInfo(Product product){
        var seller = product.getSeller();
        return ProductResponse.SellerInfo.builder()
                .id(seller.getId())
                .fistName(seller.getFirstName())
                .lastName(seller.getLastName())
                .email(seller.getEmail())
                .build();


    }

}

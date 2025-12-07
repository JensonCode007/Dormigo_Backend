package com.example.demo.mapper;


import com.example.demo.Entity.Product;
import com.example.demo.dto.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

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

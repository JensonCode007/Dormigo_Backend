package org.example.dormigobackend.mapper;


import org.example.dormigobackend.Entity.Product;
import org.example.dormigobackend.Entity.ProductImage;
import org.example.dormigobackend.Repository.ProductImageRepository;
import org.example.dormigobackend.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

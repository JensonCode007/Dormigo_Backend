package org.example.dormigobackend.mapper;

import org.example.dormigobackend.Entity.Cart;
import org.example.dormigobackend.Entity.CartItems;
import org.example.dormigobackend.Entity.ProductImage;
import org.example.dormigobackend.dto.response.CartItemResponse;
import org.example.dormigobackend.dto.response.CartResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class CartMapper {
    public static CartResponse toResponse(Cart cart) {
        if(cart == null) return null;

        List<CartItemResponse> cartItemResponseList = cart.getItems().stream()
                .map(CartMapper::toResponse)
                .toList();

        Integer totalItems = cart.getItems().stream()
                .mapToInt(CartItems::getQuantity)
                .sum();

        BigDecimal totalPrice = cart.getItems().stream()
                .map(CartItems::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .totalPrice(totalPrice)
                .totalItems(totalItems)
                .cartItems(cartItemResponseList)
                .createdDate(cart.getCreatedAt())
                .updatedDate(cart.getUpdatedAt())
                .build();
    }

    public static CartItemResponse toResponse(CartItems item){
        if(item == null) return null;

        String imageUrl = item.getProduct().getProductImages().stream()
                .filter(ProductImage::getIsPrimary)
                .map(ProductImage::getImageUrl)
                .findFirst()
                .orElse(
                        item.getProduct().getProductImages().isEmpty() ? null
                                : item.getProduct().getProductImages().get(0).getImageUrl()
                );

        return CartItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productImage(imageUrl)
                .availableStock(item.getProduct().getQuantity())
                .isAvailable(item.getProduct().getIsAvailable())
                .totalPrice(item.getTotalPrice())
                .price(item.getProduct().getPrice())
                .addedAt(item.getAddedAt())
                .productTitle(item.getProduct().getTitle())
                .quantity(item.getProduct().getQuantity())
                .build();
    }
}

package com.example.demo.mapper;

import com.example.demo.Entity.OrderItem;
import com.example.demo.dto.response.OrderItemResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public static OrderItemResponse toResponse(OrderItem orderItem){
        if (orderItem == null){
            return null;
        }
        else{
            return  OrderItemResponse.builder()
                    .id(orderItem.getId())
                    .priceAtPurchase(orderItem.getPriceAtPurchase())
                    .quantity(orderItem.getQuantity())
                    .sellerId(orderItem.getSeller().getId())
                    .sellerName(orderItem.getSeller().getFirstName() + " " + orderItem.getSeller().getLastName())
                    .productId(orderItem.getProduct().getId())
                    .productTitle(orderItem.getProduct().getTitle())
                    .subtotal(orderItem.getSubTotal())
                    .build();
        }
    }
}

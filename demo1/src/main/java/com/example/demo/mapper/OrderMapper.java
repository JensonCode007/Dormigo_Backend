package com.example.demo.mapper;

import com.example.demo.Entity.Order;
import com.example.demo.dto.response.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public static OrderResponse toResponse(Order order) {
        if (order == null)
            return null;
        else {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(order.getId());
            orderResponse.setOrderNumber(order.getOrderNumber());
            orderResponse.setBuyerId(order.getBuyer().getId());
            orderResponse.setBuyerName(order.getBuyer().getFirstName() + " " + order.getBuyer().getLastName());
            orderResponse.setBuyerEmail(order.getBuyer().getEmail());
            orderResponse.setItems(order.getItems().stream().map(OrderItemMapper::toResponse).collect(Collectors.toList()));
            orderResponse.setTotalPrice(order.getTotalAmount());
            orderResponse.setStripePaymentIntendId(order.getStripePaymentIntendId());
            orderResponse.setStripePaymentStatus(order.getStripePaymentStatus());
            orderResponse.setOrderStatus(order.getOrderStatus());
            orderResponse.setMeetingLocation(order.getMeetingLocation());
            orderResponse.setMeetingNotes(order.getMeetingNotes());
            orderResponse.setMeetingDate(order.getMeetingTime());
            orderResponse.setCreatedAt(order.getCreatedAt());
            orderResponse.setOtpGeneratedAt(order.getOtpGeneratedAt());
            orderResponse.setOtpVerifiedAt(order.getOtpVerifiedAt());
            orderResponse.setOtpExpiresAt(order.getOtpExpiresAt());
            orderResponse.setUpdatedAt(order.getUpdatedAt());
            orderResponse.setCompletedAt(order.getCompletedAt());

            return orderResponse;
        }
    }
}



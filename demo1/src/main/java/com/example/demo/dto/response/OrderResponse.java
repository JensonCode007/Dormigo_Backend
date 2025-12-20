package com.example.demo.dto.response;


import com.example.demo.Entity.OrderItem;
import com.example.demo.Enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    private Long id;
    private String orderNumber;

    private Long buyerId;
    private String buyerName;
    private String buyerEmail;

    private List<OrderItemResponse> items;
    private BigDecimal totalPrice;

    private String stripePaymentIntendId;
    private String stripePaymentStatus;

    private OrderStatus orderStatus;

    private String meetingLocation;
    private String meetingNotes;
    private LocalDateTime meetingDate;


    private LocalDateTime otpGeneratedAt;
    private LocalDateTime otpExpiresAt;
    private LocalDateTime otpVerifiedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;

}

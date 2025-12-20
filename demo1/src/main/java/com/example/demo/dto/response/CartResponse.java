package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {

    private Long id;
    private Long userId;
    private BigDecimal totalPrice;
    private Integer totalItems;
    private List<CartItemResponse> cartItems;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}

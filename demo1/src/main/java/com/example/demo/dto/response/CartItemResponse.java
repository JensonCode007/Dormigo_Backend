package com.example.demo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {

    private Long id;
    private Long productId;
    private String productImage;
    private Integer availableStock;
    private Boolean isAvailable;
    private BigDecimal totalPrice;
    private BigDecimal price;
    private LocalDateTime addedAt;
    private String productTitle;
    private Integer quantity;
}

package com.example.demo.dto.response;

import com.example.demo.Entity.Category;
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
public class ProductResponse {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private String categoryName;
    private Integer quantity;
    private String condition;
    private Boolean isAvailable;
    private SellerInfo seller;
    private LocalDateTime createdAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SellerInfo {
        private Long id;
        private String fistName;
        private String lastName;
        private String email;
    }


}

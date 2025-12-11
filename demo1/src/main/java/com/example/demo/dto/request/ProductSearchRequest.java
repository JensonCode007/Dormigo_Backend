package com.example.demo.dto.request;


import com.example.demo.Enums.ProductCondition;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSearchRequest {

    private String keyword;

    private Long categoryId;
    private ProductCondition condition;

    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    private Integer page=0;
    private Integer size=20;
    private String sortBy;
    private String sortDir;
}

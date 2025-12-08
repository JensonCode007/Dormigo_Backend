package com.example.demo.dto.request;

import com.example.demo.Enums.ProductCondition;
import jakarta. validation.constraints.*;
import lombok. AllArgsConstructor;
import lombok.Builder;
import lombok. Data;
import lombok.NoArgsConstructor;

import java. math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    // All fields optional for partial updates (PATCH)

    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    private String title;

    @Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
    private String description;

    @NotNull(message = "price is required")
    private BigDecimal price;

    @Min(value = 0, message = "Quantity cannot be negative")
    @Max(value = 1000, message = "Quantity cannot exceed 1000")
    private Integer quantity;

    private ProductCondition condition;

    private Long categoryId;

    private Boolean isAvailable;
}
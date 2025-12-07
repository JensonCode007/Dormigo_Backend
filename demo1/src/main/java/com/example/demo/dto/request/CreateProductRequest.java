package com.example.demo.dto.request;

import com.example.demo.Enums.ProductCondition;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class CreateProductRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0. 01", message = "Price must be greater than 0")
    @DecimalMax(value = "999999. 99", message = "Price cannot exceed 999,999.99")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 1000, message = "Quantity cannot exceed 1000")
    private Integer quantity;

    @NotNull(message = "Condition is required")
    private ProductCondition condition;

    @NotNull(message = "Category is required")
    private Long categoryId;
}

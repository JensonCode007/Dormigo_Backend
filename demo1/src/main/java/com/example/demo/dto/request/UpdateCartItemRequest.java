package com.example.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCartItemRequest {

    @NotNull(message = "Quantity value update is required")
    @Min(value = 1, message = "Quantity must be at least one")
    private Integer quantity;
}

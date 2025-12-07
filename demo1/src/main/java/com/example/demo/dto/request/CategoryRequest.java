package com.example.demo.dto.request;


import jakarta.validation.constraints.*;


public class CategoryRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 5000, message = "Description must be between 10 and 5000 characters")
    private String description;

}

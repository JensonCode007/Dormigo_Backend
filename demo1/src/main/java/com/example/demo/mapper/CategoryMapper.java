package com.example.demo.mapper;

import com.example.demo.Entity.Category;
import com.example.demo.dto.response.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public static CategoryResponse toResponse(Category category) {
        if (category == null) {
            return null;
        }
        else{
            return CategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .description(category.getDescription())
                    .build();
        }
    }
}

package com.example.demo.service;

import com.example.demo.Entity.Category;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.dto.request.CategoryRequest;
import com.example.demo.dto.response.CategoryResponse;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public @Nullable List<CategoryResponse> findAllCategories() {
        Collection<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryMapper::toResponse).collect(Collectors.toList());
    }

    public @Nullable Category createCategory(Category category) {
        if(categoryRepository.findByName(category.getName()).isPresent()) {
            throw new IllegalStateException("Category with name " + category.getName() + " already exists");
        }
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        if(categoryRepository.findById(id).isEmpty()) {
            throw new IllegalStateException("Category with id " + id + " does not exist");
        }
        categoryRepository.deleteById(id);
    }

    public @Nullable CategoryResponse getCategoryById(Long id) {
        if(categoryRepository.findById(id).isEmpty()) {
            throw new IllegalStateException("Category with id " + id + " does not exist");
        }
        return CategoryMapper.toResponse(categoryRepository.findById(id).get());
    }

    public CategoryResponse updateCategory(CategoryRequest categoryRequest, Long id) {
        if(categoryRepository.findById(id).isEmpty()) {
            throw new IllegalStateException("Category with id " + id + " does not exist");
        }
        Category category = new Category();
        category.setDescription(category.getDescription());
        category.setName(category.getName());
        categoryRepository.save(category);
        return CategoryMapper.toResponse(categoryRepository.findById(id).get());
    }
}

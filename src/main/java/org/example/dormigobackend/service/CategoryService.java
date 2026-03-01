package org.example.dormigobackend.service;

import org.example.dormigobackend.Entity.Category;
import org.example.dormigobackend.Repository.CategoryRepository;
import org.example.dormigobackend.dto.request.CategoryRequest;
import org.example.dormigobackend.dto.response.CategoryResponse;
import org.example.dormigobackend.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Cacheable(value = "categories",
    key = "'all'")
    public @Nullable List<CategoryResponse> findAllCategories() {
        Collection<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryMapper::toResponse).collect(Collectors.toList());
    }

    @CacheEvict(value = "categories",allEntries = true)
    public @Nullable Category createCategory(Category category) {
        if(categoryRepository.findByName(category.getName()).isPresent()) {
            throw new IllegalStateException("Category with name " + category.getName() + " already exists");
        }
        return categoryRepository.save(category);
    }

    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategory(Long id) {
        if(categoryRepository.findById(id).isEmpty()) {
            throw new IllegalStateException("Category with id " + id + " does not exist");
        }
        categoryRepository.deleteById(id);
    }
    @Cacheable(value = "categories", key = "'id:' + #id")
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

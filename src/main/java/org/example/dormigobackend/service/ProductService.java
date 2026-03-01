package org.example.dormigobackend.service;

import org.example.dormigobackend.Entity.Category;
import org.example.dormigobackend.Entity.Product;
import org.example.dormigobackend.Entity.User;
import org.example.dormigobackend.Enums.ProductCondition;
import org.example.dormigobackend.Repository.CategoryRepository;
import org.example.dormigobackend.Repository.ProductImageRepository;
import org.example.dormigobackend.Repository.ProductRepository;
import org.example.dormigobackend.Repository.UserRepository;
import org.example.dormigobackend.dto.request.ProductRequest;
import org.example.dormigobackend.dto.request.ProductSearchRequest;
import org.example.dormigobackend.dto.response.PageResponse;
import org.example.dormigobackend.dto.response.ProductResponse;
import org.example.dormigobackend.exception.ResourceNotFoundException;
import org.example.dormigobackend.exception.UnauthorizedException;
import org.example.dormigobackend.mapper.ProductMapper;
import org.example.dormigobackend.security.UserPrincipal;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProductImageRepository productImageRepository;
    private final EmailService emailService;

    @Transactional
    public ProductResponse addProduct(ProductRequest productRequest, UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", productRequest.getCategoryId()));

        if (productRequest. getPrice() == null) {
            throw new IllegalArgumentException("Price is required");
        }
        if (productRequest.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (productRequest.getPrice().compareTo(new BigDecimal("999999.99")) > 0) {
            throw new IllegalArgumentException("Price cannot exceed 999,999.99");
        }
        if (productRequest.getCondition() == null) {
            throw new IllegalArgumentException("Product condition is required");
        }
        Product product = new Product();
        product.setTitle(productRequest.getTitle());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setQuantity(productRequest.getQuantity());
        product.setProductCondition(productRequest.getCondition());
        product.setCategory(category);
        product.setSeller(user);
        product.setIsAvailable(true);

        Product savedProduct = productRepository.save(product);
        emailService.sendProductListedEmail(savedProduct);
        return ProductMapper.toResponse(savedProduct);
    }

    @Cacheable(value = "products",
    key = "'page:' + #page + 'size:' + #size + 'sortBy:' + #sortBy + 'sortDir:' + #sortDir"
    )
    public PageResponse<ProductResponse> getAllAvailableProducts(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);



        Page<ProductResponse> dbPage = productRepository.findByIsAvailableTrue(pageable)
                .map(ProductMapper::toResponse);

        return PageResponse.of(dbPage);
    }

    @Cacheable(value = "products",
    key = "'id' + #id"
    )
    public ProductResponse getProductById(Long id){


        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));


        return ProductMapper.toResponse(product);
    }

    public PageResponse<ProductResponse> getProductsByCategory(int page, int size, Long id, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductResponse> dbPage= productRepository.findByCategoryIdAndIsAvailableTrue(id, pageable)
                .map(ProductMapper::toResponse);

        return PageResponse.of(dbPage);
    }

    public Page<ProductResponse> searchProducts(String query, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.searchProducts(query, pageable)
                .map(ProductMapper::toResponse);
    }

    public List<ProductResponse> getMyProducts(UserPrincipal userPrincipal) {
        Collection<Product> product = productRepository.findBySellerId(userPrincipal.getId());
        return product.stream().map(ProductMapper::toResponse).collect(Collectors.toList());


    }

    @CacheEvict(value = "products",allEntries = true)
    @CachePut(value = "products", key = "'id:' + #id")
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productRequest, UserPrincipal userPrincipal) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if(!product.getSeller().getId().equals(userPrincipal.getId())){
            throw new UnauthorizedException("You are not allowed to update this product");
        }

        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", productRequest.getCategoryId())
        );
        if (productRequest. getTitle() != null) {
            product.setTitle(productRequest.getTitle());
        }
        if (productRequest.getPrice() != null) {
            product.setPrice(productRequest.getPrice());
        }
        if (productRequest.getDescription() != null) {
            product.setDescription(productRequest.getDescription());
        }
        if (productRequest.getQuantity() != null) {
            product.setQuantity(productRequest.getQuantity());
        }
        if (productRequest.getCondition() != null) {
            product.setProductCondition(productRequest.getCondition());
        }
        if (productRequest.getCategoryId() != null) {
            product.setCategory(category);
        }
        if (productRequest.getIsAvailable() != null) {
            product.setIsAvailable(productRequest.getIsAvailable());
        }

        Product updatedProduct = productRepository.save(product);
        return ProductMapper.toResponse(updatedProduct);

    }

    @CacheEvict(value = "products", allEntries = true)
    @Transactional
    public void deleteProduct(Long id, UserPrincipal userPrincipal) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if(!product.getSeller().getId().equals(userPrincipal.getId())){
            throw new UnauthorizedException("You are not allowed to delete this product");
        }

        productRepository.delete(product);

    }


    public Page<ProductResponse> advanceSearchFilter(ProductSearchRequest productSearchRequest) {

        int page = productSearchRequest.getPage() != null ? productSearchRequest.getPage() : 0;
        int size = productSearchRequest.getSize() != null ? productSearchRequest.getSize() : 20;
        String sortBy = productSearchRequest.getSortBy() != null ? productSearchRequest.getSortBy() : "createdAt";
        String sortDir = productSearchRequest.getSortDir() != null ? productSearchRequest.getSortDir() : "ASC";

        Sort sort = sortBy.equalsIgnoreCase("DESC")?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.searchProductsWithFilters(
                productSearchRequest.getKeyword(),
                productSearchRequest.getCategoryId(),
                productSearchRequest.getCondition(),
                productSearchRequest.getMinPrice(),
                productSearchRequest.getMaxPrice(),
                pageable
        ).map(ProductMapper::toResponse);
    }

    public Page<ProductResponse> getProductsByPriceRange(
            BigDecimal minPrice, BigDecimal maxPrice,
            int page, int size, String sortBy, String sortDir) {

        if(page < 0) page = 0;
        if(size < 0) size = 10;

        Sort sort = sortBy.equalsIgnoreCase("DESC")?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findByPriceBetweenAndIsAvailableTrue(
                minPrice,
                maxPrice,
                pageable
        ).map(ProductMapper::toResponse);
    }

    public Page<ProductResponse> getProductsByCondition(
            ProductCondition productCondition,
            int page, int size, String sortBy, String sortDir) {
        if(page < 0) page = 0;
        if(size < 0) size = 10;
        Sort sort = sortBy.equalsIgnoreCase("DESC")?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.findByProductConditionAndIsAvailableTrue(pageable, productCondition)
                                                                        .map(ProductMapper::toResponse);


    }


}

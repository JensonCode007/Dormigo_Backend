package org.example.dormigobackend.controller;


import org.example.dormigobackend.Enums.ProductCondition;
import org.example.dormigobackend.Repository.ProductRepository;
import org.example.dormigobackend.dto.request.ProductRequest;
import org.example.dormigobackend.dto.request.ProductSearchRequest;
import org.example.dormigobackend.dto.response.PageResponse;
import org.example.dormigobackend.dto.response.ProductResponse;
import org.example.dormigobackend.security.UserPrincipal;
import org.example.dormigobackend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {


    private final ProductRepository productRepository;
    private final ProductService productService;

    @GetMapping("/public/all")
    public ResponseEntity<PageResponse<ProductResponse>> getAllAvailableProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir
    ){
        if(page < 0) page = 0;
        if(size >100) size = 100;
        PageResponse<ProductResponse> product = productService.getAllAvailableProducts(page, size, sortBy, sortDir);
        return ResponseEntity.ok(product);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest productRequest,
                                                      @AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(productService.addProduct(productRequest,userPrincipal));

    }

    @GetMapping("/public/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/public/category/{categoryId}")
    public ResponseEntity<PageResponse<ProductResponse>> getProductByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "title")  String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir){
        if(page < 0) page = 0;
        if(size > 100) size = 100;
        return ResponseEntity.ok(productService.getProductsByCategory(page, size, categoryId, sortBy, sortDir));

    }

    @GetMapping("/public/search")
    public ResponseEntity<Page<ProductResponse>> getProductBySearch(
            @RequestParam  String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam String sortDir){

        if(page < 0) page = 0;
        if(size > 100) size = 100;
        return ResponseEntity.ok(productService.searchProducts(query, page, size, sortBy, sortDir));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/my-products")
    public ResponseEntity<List<ProductResponse>> getMyProducts(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(productService.getMyProducts(userPrincipal));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @RequestBody ProductRequest productRequest,
                                                         @AuthenticationPrincipal UserPrincipal userPrincipal){

        return ResponseEntity.ok(productService.updateProduct(id, productRequest, userPrincipal));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserPrincipal userPrincipal){
        productService.deleteProduct(id, userPrincipal);
        return new ResponseEntity<>("Product deleted", HttpStatus.OK);
    }

    @GetMapping("/public/advance-search")
    public ResponseEntity<Page<ProductResponse>> advancedSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) ProductCondition condition,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir

    ){
        if(page < 0) page = 0;
        if(size > 100) size = 100;

        ProductSearchRequest product = ProductSearchRequest.builder()
                .keyword(keyword)
                .categoryId(categoryId)
                .condition(condition)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDir(sortDir)
                .build();

        Page<ProductResponse> getProduct = productService.advanceSearchFilter(product);
        return ResponseEntity.ok(getProduct);
    }

    @GetMapping("/public/price-range")
    public ResponseEntity<Page<ProductResponse>> getProductsByPriceRange(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir

    ){
        if(page < 0) page = 0;
        if(size > 100) size = 100;

        Page<ProductResponse> product = productService.getProductsByPriceRange(
                minPrice, maxPrice, page, size, sortBy, sortDir
        );
        return ResponseEntity.ok(product);


    }

    @GetMapping("/public/condition/{condition}")
    public ResponseEntity<Page<ProductResponse>> getProductsByProductCondition(
            @RequestParam(required = false) ProductCondition condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ){
        if(page < 0) page = 0;
        if(size > 100) size = 100;

        Page<ProductResponse> product = productService.getProductsByCondition(
                condition, page, size, sortBy, sortDir);

        return ResponseEntity.ok(product);
    }

}

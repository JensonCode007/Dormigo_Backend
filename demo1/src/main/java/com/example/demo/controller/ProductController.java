package com.example.demo.controller;


import com.example.demo.Entity.Product;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @GetMapping("/public/all")
    public ResponseEntity<Page<ProductResponse>> getAllAvailableProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "title") String sortBy
    ){
        Page<ProductResponse> product = productService.getAllAvailableProducts(page, size, sortBy);
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
    public ResponseEntity<Page<ProductResponse>> getProductByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size){
        return ResponseEntity.ok(productService.getProductsByCategory(page, size, categoryId));

    }

    @GetMapping("/public/search")
    public ResponseEntity<Page<ProductResponse>> getProductBySearch(@RequestParam String query,
                                                                    @RequestParam int page,
                                                                    @RequestParam int size){
        return ResponseEntity.ok(productService.searchProducts(query, page, size));
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
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserPrincipal userPrincipal){
        productService.deleteProduct(id, userPrincipal);
        return ResponseEntity.ok(productService.getProductById(id));
    }




}

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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @GetMapping("/allAvailable")
    public ResponseEntity<Page<ProductResponse>> getAllAvailableProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "title") String sortBy
    ){
        Page<ProductResponse> product = productService.getAllAvailableProducts(page, size, sortBy);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest productRequest,
                                                      @AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(productService.addProduct(productRequest,userPrincipal));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/byCategory/{id}")
    public ResponseEntity<Page<ProductResponse>> getProductByCategory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size){
        return ResponseEntity.ok(productService.getProductsByCategory(page, size, id));

    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> getProductBySearch(@RequestParam String query,
                                                                    @RequestParam int page,
                                                                    @RequestParam int size){
        return ResponseEntity.ok(productService.searchProducts(query, page, size));
    }

    @GetMapping("/my-products")
    public ResponseEntity<List<ProductResponse>> getMyProducts(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(productService.getMyProducts(userPrincipal));
    }

    @PutMapping("{id")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @RequestBody ProductRequest productRequest,
                                                         @AuthenticationPrincipal UserPrincipal userPrincipal){

        return ResponseEntity.ok(productService.updateProduct(id, productRequest, userPrincipal));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserPrincipal userPrincipal){
        productService.deleteProduct(id, userPrincipal);
        return ResponseEntity.ok(productService.getProductById(id));
    }




}

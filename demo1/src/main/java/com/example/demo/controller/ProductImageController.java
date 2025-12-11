package com.example.demo.controller;

import com.example.demo.Entity.Product;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.dto.response.ProductImageResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("products/{productId}/images")
    public ResponseEntity<ProductImageResponse> uploadImage(
            @PathVariable("productId") Long productId,
            @RequestParam("image") MultipartFile file,
            @RequestParam("isPrimary") boolean isPrimary,
            @AuthenticationPrincipal UserPrincipal userPrincipal)
    {

        ProductImageResponse productImageResponse = productImageService.uploadImage(
                file, isPrimary, productId, userPrincipal
        );
        if(productImageResponse == null){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(productImageResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<ProductImageResponse> deleteImage(
            @PathVariable("imageId") Long imageId,
            @AuthenticationPrincipal UserPrincipal userPrincipal)
    {

        productImageService.deleteProductImage(imageId, userPrincipal);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/products/{productId}/images")
    public ResponseEntity<List<ProductImageResponse>> getAllImages(
            @PathVariable("productId") long productId
    ){
        List<ProductImageResponse> products = productImageService.getProductImages(productId);
        if(products == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{productId}/images/primary")
    public ResponseEntity<ProductImageResponse> getPrimaryImage(
            @PathVariable("productId") long productId
    ){
        ProductImageResponse productImageResponse = productImageService.getPrimaryImage(productId);
        return ResponseEntity.ok().body(productImageResponse);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/images/{imageId}/primary")
    public ResponseEntity<Void>  updatePrimaryImage(
            @PathVariable("imageId") long imageId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ){
        productImageService.setPrimaryImage(imageId, userPrincipal);
        return ResponseEntity.ok().build();
    }



}

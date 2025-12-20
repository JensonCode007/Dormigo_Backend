package com.example.demo.controller;


import com.example.demo.Entity.CartItems;
import com.example.demo.dto.request.AddToCartRequest;
import com.example.demo.dto.request.UpdateCartItemRequest;
import com.example.demo.dto.response.CartItemResponse;
import com.example.demo.dto.response.CartResponse;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/cart")
@PreAuthorize("hasRole('STUDENT')")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("Get cart request from user with name : {}", userPrincipal.getUsername());
        CartResponse response = cartService.getCart(userPrincipal);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItems(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                 @RequestBody AddToCartRequest addToCartRequest) {
        log.info("addItems request received : {}", addToCartRequest);
        CartResponse response = cartService.addToCart(userPrincipal, addToCartRequest);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateCart(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody UpdateCartItemRequest request,
            @PathVariable("cartItemId") Long cartItemId
            ){
        log.info("update cart item id {}", cartItemId);
        CartResponse response = cartService.updateCart(userPrincipal, request, cartItemId);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> deleteCartItem(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("cartItemId") Long cartItemId
    ){
        log.info("Delete cart item id {}", cartItemId);
        CartResponse response = cartService.deleteCartItem(userPrincipal, cartItemId);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping
    public ResponseEntity<CartResponse> clearCart(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("Clear cart {}", userPrincipal.getUsername());
        cartService.clearEntireCart(userPrincipal);
        return ResponseEntity.ok().build();
    }



}

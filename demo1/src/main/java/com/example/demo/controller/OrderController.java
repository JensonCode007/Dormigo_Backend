package com.example.demo.controller;


import com.example.demo.dto.request.CreateOrderRequest;
import com.example.demo.dto.request.OTPRequest;
import com.example.demo.dto.request.SetMeetingRequest;
import com.example.demo.dto.response.OrderResponse;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/orders")
@PreAuthorize("hasRole('STUDENT')")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Map<String,Object>> placeOrder(
            @RequestBody CreateOrderRequest createOrderRequest,
            @AuthenticationPrincipal UserPrincipal user
    )
    {
        log.info("Place Order Request Received ✅");
        Map<String,Object> response = orderService.createOrder(createOrderRequest, user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
    @PostMapping("/{orderId}/simulate-payment")
    public ResponseEntity<OrderResponse> simulatePayment(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        log.info("POST /api/orders/{}/simulate-payment - User: {}", orderId, userPrincipal.getEmail());

        OrderResponse response = orderService.simulatePaymentSuccess(orderId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{orderId}/meeting")
    public ResponseEntity<OrderResponse> createMeeting(
            @PathVariable(name = "orderId") Long orderId,
            @RequestBody SetMeetingRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ){

        log.info("Meeting Details Invoked");
        OrderResponse response = orderService.setMeetingDetails(orderId, request, userPrincipal);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{orderId}/verify-otp")
    public ResponseEntity<OrderResponse> verifyOtp(
            @PathVariable Long orderId,
            @RequestBody OTPRequest otpRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ){
        log.info("Verify OTP Request Received");
        OrderResponse response = orderService.verifyOTPAndComplete(orderId, otpRequest, userPrincipal);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<OrderResponse> deleteOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    )
    {
        log.info("Delete Order Request Received");
        OrderResponse response = orderService.cancelOrder(orderId, userPrincipal);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/get-orders")
    public ResponseEntity<Page<OrderResponse>> getOrder(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size)
    {
        log.info("Get Order Request Received");
        Page<OrderResponse> response = orderService.getMyOrders(userPrincipal,page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-sales")
    public ResponseEntity<Page<OrderResponse>> getSales(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    )
    {
        log.info("Get Sales Request Received");
        Page<OrderResponse>  response = orderService.getMySales(userPrincipal, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserPrincipal userPrincipal)
    {
        log.info("Get Order Request Received");
        OrderResponse response = orderService.getOrderById(orderId, userPrincipal);
        return ResponseEntity.ok(response);
    }




}

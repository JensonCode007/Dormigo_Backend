package com.example.demo.service;

import com.example.demo.Entity.*;
import com.example.demo.Enums.OrderStatus;
import com.example.demo.Repository.*;
import com.example.demo.dto.request.CreateOrderRequest;
import com.example.demo.dto.request.OTPRequest;
import com.example.demo.dto.request.SetMeetingRequest;
import com.example.demo.dto.response.OrderResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final MockPaymentService mockPaymentService;
    private final OtpService otpService;
    private final CartRepository cartRepository;
    private final EmailService emailService;

    @Transactional
    public Map<String, Object> createOrder(CreateOrderRequest createOrderRequest, UserPrincipal userPrincipal) {

        log.info("Creating Order Request for the User Id : {}", userPrincipal.getId());

        Cart cart = cartRepository.findByUserIdWithItems(userPrincipal.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found for the user with id : " + userPrincipal.getId())

        );

        if (cart.getItems().isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        User buyer = userRepository.findById(userPrincipal.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User not found for the user with id : " + userPrincipal.getId())
        );

        String orderNumber = generateOrderNumber();
        BigDecimal totalPrice = cart.getItems().stream()
                .map(CartItems::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String clientSecret = mockPaymentService.createPaymentIntent(totalPrice, orderNumber);
        String paymentIntendId = clientSecret.split("_secret_")[0];
        Order createOrder = Order.builder()
                .orderNumber(orderNumber)
                .buyer(buyer)
                .stripePaymentIntendId(paymentIntendId)
                .stripePaymentStatus("requires_payment_method")
                .totalAmount(totalPrice)
                .orderStatus(OrderStatus.PENDING_PAYMENT)
                .build();

        for (CartItems item : cart.getItems()) {
            Product product = item.getProduct();

            if (product.getQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Product quantity must be greater than or equal to quantity");
            }

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(item.getQuantity())
                    .seller(product.getSeller())
                    .priceAtPurchase(product.getPrice())
                    .order(createOrder)
                    .build();

            createOrder.addItem(orderItem);

            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        }
        createOrder = orderRepository.save(createOrder);


        cart.clearItems();
        cartRepository.save(cart);
        emailService.sendOrderConfirmationEmail(createOrder);

        log.info("Order Created Successfully for the User Id : {}", userPrincipal.getId());




        Map<String, Object> response = new HashMap<>();
        response.put("clientSecret", clientSecret);
        response.put("orderId", createOrder.getId());
        response.put("orderNumber", orderNumber);
        response.put("totalAmount", totalPrice);


        return response;
    }

        @Transactional
        public OrderResponse simulatePaymentSuccess (Long orderId){
            Order order = orderRepository.findById(orderId).orElseThrow(
                    () -> new ResourceNotFoundException("Order", "id", orderId)
            );
            if (!order.getOrderStatus().equals(OrderStatus.PENDING_PAYMENT)) {
                throw new IllegalStateException("Order Status is not PENDING_PAYMENT");
            }

            String paymentIntendId = order.getStripePaymentIntendId();

            mockPaymentService.confirmPayment(paymentIntendId);

            order.setStripePaymentIntendId(paymentIntendId);
            order.setStripePaymentStatus("requires_capture");
            order.setOrderStatus(OrderStatus.PAYMENT_COMPLETED);
            emailService.sendPaymentConfirmation(order);

            order = orderRepository.save(order);

            log.info("'Payment success simulation is done✅");

            notifySeller(order);

            return OrderMapper.toResponse(order);
        }

        @Transactional
        public OrderResponse setMeetingDetails (
                Long orderId,
                SetMeetingRequest meetingRequest,
                UserPrincipal userPrincipal)

        {

            Order order =  orderRepository.findById(orderId).orElseThrow(
                    () -> new ResourceNotFoundException("Order", "id", orderId)
            );

            boolean seller = order.getItems().stream().anyMatch(
                    item -> item.getSeller().getId().equals(userPrincipal.getId()));

            if(!seller){
                throw new IllegalStateException("This order does not belong to the user with id : " + userPrincipal.getId());
            }


            if(order.getOrderStatus() != OrderStatus.PAYMENT_COMPLETED
               && order.getOrderStatus() != OrderStatus.SELLER_NOTIFIED
            ){
                throw new IllegalStateException("The order is not ready sharing meeting details");
            }
            order.setMeetingLocation(meetingRequest.getMeetingLocation());
            order.setMeetingTime(meetingRequest.getMeetingTime());
            order.setMeetingNotes(meetingRequest.getMeetingNotes());
            order.setOrderStatus(OrderStatus.MEETING_ARRANGED);

            order = orderRepository.save(order);

            log.info("Meeting requirements for the order has been successfully set ✅");
            generateAndSendOTP(order);

            String otpCode = order.getOtpCode();

            emailService.sendMeetingDetails(order, otpCode);

            return OrderMapper.toResponse(order);

        }

        @Transactional
        public OrderResponse verifyOTPAndComplete(Long orderId, OTPRequest otpRequest, UserPrincipal userPrincipal){
            Order order = orderRepository.findById(orderId).orElseThrow(
                    () -> new ResourceNotFoundException("Order", "id", orderId)
            );

            boolean seller = order.getItems().stream().anyMatch(
                    item -> item.getSeller().getId().equals(userPrincipal.getId()));
            if(!seller){
                throw new IllegalStateException("This order does not belong to the user with id : " + userPrincipal.getId());
            }

            if(!order.getOrderStatus().equals(OrderStatus.OTP_GENERATED)){

                throw new ResourceNotFoundException("OTP is not even generated yet");
            }

            boolean isValid = otpService.verifyOTP(
                    otpRequest.getOtpCode(),
                    order.getOtpCode(),
                    order.getOtpExpiresAt()
            );

            if(!isValid){
                throw new IllegalArgumentException("OTP is not valid");
            }
            order.setOtpVerifiedAt(LocalDateTime.now());
            order.setOrderStatus(OrderStatus.COMPLETED);

            order = orderRepository.save(order);
            log.info("OTP has been successfully verified ✅");

            mockPaymentService.capturePayment(order.getStripePaymentIntendId());
            order.setStripePaymentStatus("Succeeded");

            order = orderRepository.save(order);
            log.info("✅ Payment captured for order: {}", order. getOrderNumber());
            return  OrderMapper.toResponse(order);


        }


        @Transactional
        public OrderResponse cancelOrder(Long  orderId, UserPrincipal userPrincipal){

            Order order = orderRepository.findById(orderId).orElseThrow(
                    () -> new ResourceNotFoundException("Order", "id", orderId)
            );

            boolean buyer = order.getBuyer().getId().equals(userPrincipal.getId());

            if(!buyer){
                throw new IllegalStateException("This order does not belong to the user with id : " + userPrincipal.getId());
            }
            if(order.getOrderStatus() == OrderStatus.COMPLETED){
                throw new IllegalStateException("Cannot cancel an order that has been completed");
            }

            for(OrderItem item : order.getItems()){
                Product product =  item.getProduct();
                product.setQuantity(product.getQuantity() + item.getQuantity());
                productRepository.save(product);
            }

            if(order.getStripePaymentIntendId() != null){
                mockPaymentService.cancelPayment(order.getStripePaymentIntendId());
                order.setStripePaymentStatus("Cancelled");
            }



            order.setOrderStatus(OrderStatus.CANCELLED);
            order = orderRepository.save(order);

            log.info("Successfully Cancelled the order with id : {} ",orderId);

            return OrderMapper.toResponse(order);
        }

        public Page<OrderResponse> getMyOrders(UserPrincipal userPrincipal, int page, int size){
            Pageable pageable = PageRequest.of(page, size);
            return orderRepository.findByBuyerId(userPrincipal.getId(), pageable).map(OrderMapper::toResponse);
        }

        public Page<OrderResponse> getMySales(UserPrincipal userPrincipal, int page, int size){
            Pageable pageable = PageRequest.of(page, size);
            return orderRepository.findBySellerId(userPrincipal.getId(), pageable).map(OrderMapper::toResponse);
        }

        public OrderResponse getOrderById(Long orderId, UserPrincipal userPrincipal){
            Order order = orderRepository.findById(orderId).orElseThrow(
                    () -> new ResourceNotFoundException("Order", "id", orderId)
            );
            boolean isBuyer = order.getBuyer().getId().equals(userPrincipal.getId());
            boolean isSeller = order.getItems().stream().anyMatch(
                    item -> item.getProduct().getId().equals(userPrincipal.getId())
            );
            if(!isBuyer && !isSeller){
                throw new IllegalStateException("This order does not belong to the user with id : " + userPrincipal.getId());
            }

            return OrderMapper.toResponse(order);

        }

        public void generateAndSendOTP(Order order){

            String otpCode = otpService.generateOTPCode();
            LocalDateTime expirationDate = otpService.getExpiryDate();

            order.setOtpCode(otpCode);
            order.setOtpGeneratedAt(LocalDateTime.now());
            order.setOtpExpiresAt(expirationDate);
            order.setOrderStatus(OrderStatus.OTP_GENERATED);
            orderRepository.save(order);
            log.info("✅ OTP generated:   {}", otpCode);
            log.info("📧 Sending OTP to buyer:  {}", order.getBuyer().getEmail());

            otpService.sendOTPToBuyer(order.getBuyer().getEmail(), otpCode);

        }

        @Transactional
        public void notifySeller (Order order){
            order.setOrderStatus(OrderStatus.SELLER_NOTIFIED);
            orderRepository.save(order);
            emailService.sendSellerNotification(order);
            log.info("Order Notification Successfully for the Order Id : {} ✅", order.getId());
        }

        public String generateOrderNumber () {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            int random = (int) (Math.random() * 1000);
            return String.format("ORD-%s-%03d", timestamp, random);
        }



}



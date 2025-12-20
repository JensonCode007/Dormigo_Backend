package com.example.demo.Entity;


import com.example.demo.Enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", unique = true, nullable = false)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "stripe_payment_intent_id", unique = true, nullable = false)
    private String stripePaymentIntendId;

    @Column(name = "stripe_payment_status", nullable = false)
    private String stripePaymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.PENDING_PAYMENT;

    @Column(name = "meeting_location", nullable = false, length = 500)
    private String meetingLocation;

    @Column(name = "meeting_time")
    private LocalDateTime meetingTime;

    @Column(name = "meeting_notes", nullable = false, length = 2000)
    private String meetingNotes;

    @Column(name = "otp_code")
    private String otpCode;

    @Column(name = "otp_generated_at")
    private LocalDateTime otpGeneratedAt;

    @Column(name = "otp_expires_at")
    private LocalDateTime otpExpiresAt;

    @Column(name = "otp_verified_at")
    private LocalDateTime otpVerifiedAt;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;


    public void addItem(OrderItem item){
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item){
        items.remove(item);
        item.setOrder(null);
    }

    public boolean otpValid(){
        return (otpCode != null && otpExpiresAt != null &&
                LocalDateTime.now().isBefore(otpExpiresAt));
    }

    public boolean otpVerified(){
        return otpVerifiedAt != null;
    }


}

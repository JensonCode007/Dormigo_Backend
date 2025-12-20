package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Table(name = "order_item")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;


    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, name = "price_at_purchase")
    private BigDecimal priceAtPurchase;

    @Column(nullable = false, name = "sub_total")
    private BigDecimal subTotal;

    @PrePersist
    @PreUpdate
    public void calculateSubTotal(){
        if(priceAtPurchase!=null && quantity!=null){
            subTotal = priceAtPurchase.multiply(BigDecimal.valueOf(quantity));
        }
    }


}

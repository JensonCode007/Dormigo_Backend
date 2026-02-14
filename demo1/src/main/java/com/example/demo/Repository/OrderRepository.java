package com.example.demo.Repository;

import com.example.demo.Entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    Page<Order> findByBuyerId(Long buyerId, Pageable pageable);

    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN o.items oi " +
            "WHERE oi.seller.id = :sellerId") // Make sure parameter name matches
    Page<Order> findBySellerId(@Param("sellerId") Long sellerId, Pageable pageable);
}

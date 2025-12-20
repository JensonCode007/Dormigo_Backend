package com.example.demo.Repository;

import com.example.demo.Entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems, Long> {

    Optional<CartItems> findByCartIdAndProductId(Long cartId, Long productId);

    void deleteByCartIdAndProductId(Long cartId, Long productId);
}

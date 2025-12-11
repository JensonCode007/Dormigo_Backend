package com.example.demo.Repository;

import com.example.demo.Entity.Product;
import com.example.demo.Entity.User;
import com.example.demo.Enums.ProductCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findByIsAvailableTrue(Pageable pageable);

    Page<Product> findByCategoryIdAndIsAvailableTrue(Long id, Pageable pageable);

    List<Product> findBySeller(User seller);

    Page<Product> findByProductConditionAndIsAvailableTrue(Pageable pageable, ProductCondition condition);

    Page<Product> findByPriceBetweenAndIsAvailableTrue(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );

    /**
     * Search products by title (case-insensitive, partial match)
     * SQL: SELECT * FROM products WHERE LOWER(title) LIKE LOWER(?) AND is_available = true
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) AND p.isAvailable = true")
    Page<Product> searchByTitle(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Search products by title OR description
     * JPQL (Java Persistence Query Language) - not native SQL
     */
    @Query("SELECT p FROM Product p WHERE " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "p.isAvailable = true")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);


    Collection<Product> findBySellerId(Long id);


    /*Advanced Filtering*/
    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR " +
            "  LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "  LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:condition is NULL OR p.productCondition = :condition) AND " +
            "(:minPrice is NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice is NULL OR p.price <= :maxPrice) AND " +
            "p.isAvailable = true"
    )
    Page<Product> searchProductsWithFilters(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("condition") ProductCondition condition,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );


    Page<Product> findByCategoryIdAndProductConditionAndIsAvailableTrue(
            Long id,
            ProductCondition condition,
            Pageable pageable
    );

    @Query("SELECT p FROM Product p WHERE " +
            "p.category.id = :categoryId AND " +
            "p.price BETWEEN :minPrice AND :maxPrice AND " +
            "p.isAvailable= true"
    )
    Page<Product> findByCategoryAndPriceRange(
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );
}

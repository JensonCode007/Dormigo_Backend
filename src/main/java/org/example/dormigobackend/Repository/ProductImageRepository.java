package org.example.dormigobackend.Repository;

import org.example.dormigobackend.Entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {


    List<ProductImage> findByProductId(Long id);

    Optional<ProductImage> findByProductIdAndIsPrimaryTrue(Long id);

    long countByProductId(Long id);

    void deleteByProductId(Long id);
}

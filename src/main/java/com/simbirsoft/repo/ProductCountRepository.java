package com.simbirsoft.repo;

import com.simbirsoft.model.Product;
import com.simbirsoft.model.ProductCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductCountRepository extends JpaRepository<ProductCount, Long> {
    Optional<ProductCount> findByProduct(Product product);
}

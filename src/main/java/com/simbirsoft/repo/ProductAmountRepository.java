package com.simbirsoft.repo;

import com.simbirsoft.model.Product;
import com.simbirsoft.model.ProductAmount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductAmountRepository extends JpaRepository<ProductAmount, Long> {
    Optional<ProductAmount> findByProduct(Product product);
}

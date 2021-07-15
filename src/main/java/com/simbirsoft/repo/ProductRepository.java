package com.simbirsoft.repo;

import com.simbirsoft.model.Group;
import com.simbirsoft.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNameAndPrice(String name, Double price);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByGroup(Group group);
}

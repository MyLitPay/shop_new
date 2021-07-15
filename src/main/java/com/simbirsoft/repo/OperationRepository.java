package com.simbirsoft.repo;

import com.simbirsoft.model.Check;
import com.simbirsoft.model.Operation;
import com.simbirsoft.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {
     List<Operation> findByCheck(Check check);

     @Query(value = "SELECT SUM(prod_amount) FROM operations WHERE " +
             "check_id = ?1 AND prod_id = ?2 GROUP BY prod_id",
             nativeQuery = true)
     Integer findAmountOfProductByCheckAndProduct(Check check, Product product);
}

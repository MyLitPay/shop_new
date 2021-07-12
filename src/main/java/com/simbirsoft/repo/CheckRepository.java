package com.simbirsoft.repo;

import com.simbirsoft.model.Check;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckRepository extends JpaRepository<Check, Long> {
}

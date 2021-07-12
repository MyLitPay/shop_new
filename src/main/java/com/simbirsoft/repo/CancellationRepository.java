package com.simbirsoft.repo;

import com.simbirsoft.model.Cancellation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancellationRepository extends JpaRepository<Cancellation, Long> {
}

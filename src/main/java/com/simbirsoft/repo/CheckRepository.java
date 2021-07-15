package com.simbirsoft.repo;

import com.simbirsoft.model.Check;
import com.simbirsoft.model.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CheckRepository extends JpaRepository<Check, Long> {
    @Query(value = "SELECT checks.id, checks.date, description, checks.total_sum, checks.closed " +
            "FROM checks JOIN operations ON checks.id = operations.check_id " +
            "WHERE checks.closed = true AND operations.operation = ?1",
            nativeQuery = true)
    List<Check> findClosedChecks(String operation);

    @Query(value = "SELECT checks.id, checks.date, description, checks.total_sum, checks.closed " +
            "FROM checks JOIN operations ON checks.id = operations.check_id " +
            "WHERE checks.closed = true AND operations.operation = ?1 AND " +
            "(checks.date BETWEEN ?2 AND ?3)",
            nativeQuery = true)
    List<Check> findClosedChecksByDateBetween(String operation, LocalDate from, LocalDate to);
}

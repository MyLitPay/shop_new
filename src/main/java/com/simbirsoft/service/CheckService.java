package com.simbirsoft.service;

import com.simbirsoft.api.dto.CheckDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.model.Check;
import com.simbirsoft.model.OperationType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CheckService {
    List<CheckDto> getAllChecks();
    CheckDto addCheck(CheckDto dto);
    List<CheckDto> updateAllChecks(List<CheckDto> request);
    void deleteAllChecks();
    CheckDto getCheckById(Long id);
    CheckDto updateCheckById(Long id, CheckDto dto);
    void deleteCheckById(Long id);

    Check findCheckById(long id);
    List<Check> findClosedChecks(OperationType operationType);
    List<Check> findClosedChecksByDateBetween(OperationType operationType,
                                              String dateFrom, String dateTo);
}

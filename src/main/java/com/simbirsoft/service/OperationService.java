package com.simbirsoft.service;

import com.simbirsoft.api.dto.OperationDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.model.Operation;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OperationService {
    ResponseEntity<List<OperationDto>> getAllOperations();
    OperationDto addOperation(OperationDto dto);
    ResponseEntity<List<OperationDto>> updateAllOperations(List<OperationDto> request);
    ResultResponse deleteAllOperations();
    OperationDto getOperationById(Long id);
    OperationDto updateOperationById(Long id, OperationDto dto);
    void deleteOperationById(Long id);

    Operation findOperationById(long id);
}

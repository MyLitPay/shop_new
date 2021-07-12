package com.simbirsoft.service;

import com.simbirsoft.api.dto.OperationDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.model.Operation;

import java.util.List;

public interface OperationService {
    List<OperationDto> getAllOperations();
    OperationDto addOperation(OperationDto dto);
    List<OperationDto> updateAllOperations(List<OperationDto> request);
    ResultResponse deleteAllOperations();
    OperationDto getOperationById(Long id);
    OperationDto updateOperationById(Long id, OperationDto dto);
    ResultResponse deleteOperationById(Long id);

    Operation findOperationById(long id);
}

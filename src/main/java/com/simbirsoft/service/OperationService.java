package com.simbirsoft.service;

import com.simbirsoft.api.dto.OperationDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.model.Check;
import com.simbirsoft.model.Operation;
import com.simbirsoft.model.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OperationService {
    List<OperationDto> getAllOperations();
    OperationDto addOperation(OperationDto dto);
    List<OperationDto> updateAllOperations(List<OperationDto> request);
    void deleteAllOperations();
    OperationDto getOperationById(Long id);
    OperationDto updateOperationById(Long id, OperationDto dto);
    void deleteOperationById(Long id);

    Operation findOperationById(long id);
    List<Operation> findOperationListByCheck(Check check);
    Integer findAmountOfProductByCheckAndProduct(Check check, Product product);
}

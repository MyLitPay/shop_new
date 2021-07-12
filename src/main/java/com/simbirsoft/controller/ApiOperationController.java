package com.simbirsoft.controller;

import com.simbirsoft.api.dto.OperationDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.service.OperationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operations")
public class ApiOperationController {
    final OperationService operationService;

    public ApiOperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @GetMapping
    public List<OperationDto> getAllOperations() {
        return operationService.getAllOperations();
    }

    @PostMapping
    public OperationDto addOperation(@RequestBody OperationDto dto) {
        return operationService.addOperation(dto);
    }

    @PutMapping
    public List<OperationDto> updateAllOperations(@RequestBody List<OperationDto> request) {
        return operationService.updateAllOperations(request);
    }

    @DeleteMapping
    public ResultResponse deleteAllOperations() {
        return operationService.deleteAllOperations();
    }

    @GetMapping("/{id}")
    public OperationDto getOperationById(@PathVariable("id") Long id) {
        return operationService.getOperationById(id);
    }

    @PutMapping("/{id}")
    public OperationDto updateOperationById(@PathVariable("id") Long id,
                                        @RequestBody OperationDto dto) {
        return operationService.updateOperationById(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteOperationById(@PathVariable("id") Long id) {
        operationService.deleteOperationById(id);
    }
}

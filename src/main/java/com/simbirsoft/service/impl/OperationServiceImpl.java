package com.simbirsoft.service.impl;

import com.simbirsoft.api.dto.OperationDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.api.response.ResultResponseType;
import com.simbirsoft.exception.*;
import com.simbirsoft.mapper.OperationMapper;
import com.simbirsoft.model.*;
import com.simbirsoft.repo.CancellationRepository;
import com.simbirsoft.repo.CheckRepository;
import com.simbirsoft.repo.OperationRepository;
import com.simbirsoft.repo.ProductRepository;
import com.simbirsoft.service.OperationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OperationServiceImpl implements OperationService {
    final OperationRepository operationRepository;
    final ProductRepository productRepository;
    final CheckRepository checkRepository;
    final CancellationRepository cancellationRepository;

    public OperationServiceImpl(OperationRepository operationRepository, ProductRepository productRepository, CheckRepository checkRepository, CancellationRepository cancellationRepository) {
        this.operationRepository = operationRepository;
        this.productRepository = productRepository;
        this.checkRepository = checkRepository;
        this.cancellationRepository = cancellationRepository;
    }

    @Override
    public List<OperationDto> getAllOperations() {
        List<OperationDto> operations = new ArrayList<>();

        for (Operation operation : operationRepository.findAll()) {
            OperationDto dto = OperationMapper.INSTANCE.toDTO(operation);
            dto.setOperation(operation.getOperation().name());
            operations.add(dto);
        }
        if (operations.isEmpty()) {
            throw new CancellationNotFoundException("Operations not found");
        }
        return operations;
    }

    @Override
    public OperationDto addOperation(OperationDto dto) {
        Operation operation = OperationMapper.INSTANCE.toEntity(dto);
        operation.setOperation(stringToEnum(dto.getOperation()));
        if (dto.getProdId() == null) {
            throw new ProductNotSetException("Product don't set");
        }
        Operation operationFromDB = operationRepository.saveAndFlush(setConstraints(dto, operation));
        return OperationMapper.INSTANCE.toDTO(operationFromDB);
    }

    @Override
    public List<OperationDto> updateAllOperations(List<OperationDto> request) {
        List<Operation> operationList = new ArrayList<>();

        for (OperationDto dto : request) {
            Operation operation = updateOperationData(findOperationById(dto.getId()), dto);
            operationList.add(operation);
        }
        operationRepository.saveAll(operationList);
        return getAllOperations();
    }

    @Override
    public ResultResponse deleteAllOperations() {
        try {
            List<Operation> operations = operationRepository.findAll();
            for (Operation operation : operations) {
                deleteConstraints(operation);
            }
            operationRepository.deleteAll(operations);
            return new ResultResponse(ResultResponseType.OK);
        } catch (Exception ex) {
            return new ResultResponse(ResultResponseType.ERROR);
        }
    }

    @Override
    public OperationDto getOperationById(Long id) {
        Operation operation = findOperationById(id);
        if (operation.getProduct() == null) {
            operation.setProduct(new Product());
        }
        if (operation.getCheck() == null) {
            operation.setCheck(new Check());
        }
        if (operation.getCancellation() == null) {
            operation.setCancellation(new Cancellation());
        }
        OperationDto dto = OperationMapper.INSTANCE.toDTO(operation);
        dto.setOperation(operation.getOperation().name());
        return dto;
    }

    @Override
    public OperationDto updateOperationById(Long id, OperationDto dto) {
        Operation operation = updateOperationData(findOperationById(id), dto);
        Operation operationFromDB = operationRepository.saveAndFlush(operation);
        return OperationMapper.INSTANCE.toDTO(operationFromDB);
    }

    @Override
    public void deleteOperationById(Long id) {
            deleteConstraints(findOperationById(id));
            operationRepository.deleteById(id);
    }

    public Operation findOperationById(long id) {
        return operationRepository.findById(id)
                .orElseThrow(() -> new OperationNotFoundException("Operation not found"));
    }

    private Operation updateOperationData(Operation operation, OperationDto dto) {
        operation.setAmount(dto.getAmount());
        operation.setSum(dto.getSum());
        operation.setOperation(stringToEnum(dto.getOperation()));
        return setConstraints(dto, operation);
    }

    private OperationType stringToEnum(String typeName) {
        OperationType type;
        switch (typeName) {
            case "SELLING" : type = OperationType.SELLING;
                break;
            case "RESCUE" : type = OperationType.RESCUE;
                break;
            default: type = OperationType.NOT_DEFINE;
                break;
        }
        return type;
    }

    private Operation setConstraints(OperationDto dto, Operation operation) {
        if (dto.getProdId() != null) {
            Product product = getProductFromDB(dto.getProdId());
            operation.setProduct(product);
            if (product.getOperations() == null) {
                product.setOperations(new ArrayList<>());
            }
            product.getOperations().add(operation);
        }
        if (dto.getCheckId() != null) {
            Check check = getCheckFromDB(dto.getCheckId());
            operation.setCheck(check);
            if (check.getOperations() == null) {
                check.setOperations(new ArrayList<>());
            }
            check.getOperations().add(operation);
        } else {
            operation.setCheck(new Check());
        }
        if (dto.getCancellationId() != null) {
            Cancellation cancellation = getCancellationFromDB(dto.getCancellationId());
            operation.setCancellation(cancellation);
            if (cancellation.getOperations() == null) {
                cancellation.setOperations(new ArrayList<>());
            }
            cancellation.getOperations().add(operation);
        } else {
            operation.setCancellation(new Cancellation());
        }
        return operation;
    }

    private void deleteConstraints(Operation operation) {
        if (operation.getProduct() != null) {
            Product product = getProductFromDB(operation.getProduct().getId());
            product.getOperations().remove(operation);
        }
        if (operation.getCheck() != null) {
            Check check = getCheckFromDB(operation.getCheck().getId());
            check.getOperations().remove(operation);
        }
        if (operation.getCancellation() != null) {
            Cancellation cancellation = getCancellationFromDB(operation.getCancellation().getId());
            cancellation.getOperations().remove(operation);
        }
    }

    private Product getProductFromDB(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    private Check getCheckFromDB(Long id) {
        return checkRepository.findById(id)
                .orElseThrow(() -> new CheckNotFoundException("Check not found"));
    }

    private Cancellation getCancellationFromDB(Long id) {
        return cancellationRepository.findById(id)
                .orElseThrow(() -> new CancellationNotFoundException("Cancellation not found"));
    }
}

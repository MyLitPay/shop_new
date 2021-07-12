package com.simbirsoft.service.impl;

import com.simbirsoft.api.dto.*;
import com.simbirsoft.api.response.CancellationResponse;
import com.simbirsoft.api.response.CheckResponse;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.api.response.ResultResponseType;
import com.simbirsoft.mapper.CheckMapper;
import com.simbirsoft.mapper.InvoiceDtoToProductDtoMapper;
import com.simbirsoft.mapper.ProductAmountMapper;
import com.simbirsoft.model.Check;
import com.simbirsoft.model.Operation;
import com.simbirsoft.model.Product;
import com.simbirsoft.model.ProductAmount;
import com.simbirsoft.service.*;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GeneralServiceImpl implements GeneralService {
    final GroupService groupService;
    final InvoiceService invoiceService;
    final ProductService productService;
    final ProductAmountService productAmountService;
    final CheckService checkService;
    final CancellationService cancellationService;
    final OperationService operationService;

    public GeneralServiceImpl(GroupService groupService, InvoiceService invoiceService,
                              ProductService productService, ProductAmountService productAmountService,
                              CheckService checkService, CancellationService cancellationService,
                              OperationService operationService) {
        this.groupService = groupService;
        this.invoiceService = invoiceService;
        this.productService = productService;
        this.productAmountService = productAmountService;
        this.checkService = checkService;
        this.cancellationService = cancellationService;
        this.operationService = operationService;
    }

    public ResultResponse addProductByInvoiceId(Long invoiceId) {
        try {
            InvoiceDto invoiceDto = invoiceService.getInvoiceById(invoiceId);
            Product product = productService.findByNameAndPrice(invoiceDto.getName(), invoiceDto.getPrice());
            if (product == null) {
                ProductDto productDto = InvoiceDtoToProductDtoMapper.INSTANCE.toProductDto(invoiceDto);
                ProductDto productDtoFromDB = productService.addProduct(productDto);


                ProductAmountDto productAmountDto = InvoiceDtoToProductDtoMapper.INSTANCE.toProductAmountDto(invoiceDto);
                productAmountDto.setProdId(productDtoFromDB.getId());
                productAmountService.addProductAmount(productAmountDto);

            } else {
                ProductAmount productAmount = productAmountService.findByProduct(product);
                productAmount.setAmount(productAmount.getAmount() + invoiceDto.getAmount());
                productAmountService.updateProductAmountById(productAmount.getId(), ProductAmountMapper.INSTANCE.toDTO(productAmount));
            }
            return new ResultResponse(ResultResponseType.OK);

        } catch (Exception ex) {
            return new ResultResponse(ResultResponseType.ERROR);
        }
    }

    @Override
    public CheckResponse addProductToCheck(OperationDto operationDto) {
        OperationDto operationDtoFromDB = operationService.addOperation(operationDto);
        Check check = checkService.findCheckById(operationDtoFromDB.getCheckId());

        List<Product> productList = check.getOperations().stream()
                .map(Operation::getProduct)
                .collect(Collectors.toList());

        List<ProductForCheckDto> productForCheckDtoList = new ArrayList<>();
        for (Product product : productList) {
            ProductForCheckDto productForCheckDto = new ProductForCheckDto();
            productForCheckDto.setId(product.getId());
            productForCheckDto.setProductName(product.getName());
            productForCheckDto.setPrice(product.getPrice());
            productForCheckDto.setGroupName(product.getGroup().getName());
            productForCheckDto.setAmount(operationDto.getAmount());
            productForCheckDto.setSum(operationDto.getSum());

            if (productForCheckDtoList.contains(productForCheckDto)) {
                continue;
            }
            productForCheckDtoList.add(productForCheckDto);
        }

        Map<Product, Integer> productMap = new HashMap<>();
        for (Operation operation : check.getOperations()) {
            if (productMap.containsKey(operation.getProduct())) {
                Integer productAmount = productMap.get(operation.getProduct());
                Integer sumAmount = productAmount + operation.getAmount();

                ProductForCheckDto prod = productForCheckDtoList.get(productForCheckDtoList
                        .indexOf(new ProductForCheckDto(operation.getProduct().getName(),
                                operation.getProduct().getPrice())));
                prod.setAmount(sumAmount);
                prod.setSum(prod.getAmount() * prod.getPrice());

                productMap.put(operation.getProduct(), sumAmount);

            } else {
                productMap.put(operation.getProduct(), operation.getAmount());
            }
        }

        Double totalSum = check.getOperations().stream()
                .map(Operation::getSum)
                .reduce(Double::sum).get();

        CheckResponse response = new CheckResponse();
        response.setId(check.getId());
        response.setDescription(check.getDescription());
        response.setTotalSum(totalSum);

        DateFormat df = new SimpleDateFormat("dd_MM_yyyy HH:mm:ss");
        response.setDate(df.format(new Date()));
        response.setProductList(productForCheckDtoList);

        return response;
    }

    @Override
    public ResultResponse closeCheck(CheckResponse checkResponse) {
        return close(checkResponse.getProductList(),
                checkResponse.getId(), checkResponse.getTotalSum());
    }

    public CancellationResponse addProductForCancellation(OperationDto operationDto) {
        CheckResponse checkResponse = addProductToCheck(operationDto);
        CancellationResponse response = new CancellationResponse();
        response.setId(checkResponse.getId());
        response.setReason(checkResponse.getDescription());
        response.setProductList(checkResponse.getProductList());
        response.setDate(checkResponse.getDate());
        response.setTotalSum(checkResponse.getTotalSum());

        return response;
    }

    @Override
    public ResultResponse closeCancellationCheck(CancellationResponse cancellationResponse) {

        return close(cancellationResponse.getProductList(),
                cancellationResponse.getId(), cancellationResponse.getTotalSum());
    }

    private ResultResponse close(List<ProductForCheckDto> dtoList, Long id, Double totalSum) {
        ResultResponse response;
        List<String> errors = new ArrayList<>();
        for (ProductForCheckDto product : dtoList) {
            Product prod = productService.findProductById(product.getId());
            ProductAmount amount = productAmountService.findByProduct(prod);
            if (amount.getAmount() < product.getAmount()) {
                errors.add(product.getProductName() + " - not enough");
            }
        }

        if (errors.isEmpty()) {
            for (ProductForCheckDto product : dtoList) {
                Product prod = productService.findProductById(product.getId());
                ProductAmount amount = productAmountService.findByProduct(prod);
                amount.setAmount(amount.getAmount() - product.getAmount());
                productAmountService.updateProductAmountById(amount.getId(), ProductAmountMapper.INSTANCE.toDTO(amount));
                CheckDto checkDto = CheckMapper.INSTANCE.toDTO(checkService.findCheckById(id));
                checkDto.setTotalSum(totalSum);
                checkDto.setDate(new Date());
                checkService.updateCheckById(id, checkDto);
            }
            response = new ResultResponse(ResultResponseType.OK);

        } else {
            response = new ResultResponse(ResultResponseType.ERROR);
            response.setErrors(errors);
        }

        return response;
    }
}

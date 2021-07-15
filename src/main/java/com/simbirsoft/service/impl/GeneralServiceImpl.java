package com.simbirsoft.service.impl;

import com.simbirsoft.api.dto.*;
import com.simbirsoft.api.response.*;
import com.simbirsoft.exception.ClosedCheckException;
import com.simbirsoft.mapper.*;
import com.simbirsoft.model.*;
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
    final ProductCountService productCountService;
    final CheckService checkService;
    final OperationService operationService;

    public GeneralServiceImpl(GroupService groupService, InvoiceService invoiceService,
                              ProductService productService, ProductCountService productCountService,
                              CheckService checkService, OperationService operationService) {
        this.groupService = groupService;
        this.invoiceService = invoiceService;
        this.productService = productService;
        this.productCountService = productCountService;
        this.checkService = checkService;
        this.operationService = operationService;
    }

    public void addProductByInvoiceId(Long invoiceId) {
            InvoiceDto invoiceDto = invoiceService.getInvoiceById(invoiceId);
            Product product = productService.findByNameAndPrice(invoiceDto.getName(), invoiceDto.getPrice());
            if (product == null) {
                ProductDto productDto = InvoiceDtoToProductDtoMapper.INSTANCE.toProductDto(invoiceDto);
                ProductDto productDtoFromDB = productService.addProduct(productDto);


                ProductCountDto productCountDto = InvoiceDtoToProductDtoMapper.INSTANCE.toProductAmountDto(invoiceDto);
                productCountDto.setProdId(productDtoFromDB.getId());
                productCountService.addProductAmount(productCountDto);

            } else {
                ProductCount productCount = productCountService.findByProduct(product);
                productCount.setCount(productCount.getCount() + invoiceDto.getCount());
                productCountService.updateProductAmountById(productCount.getId(), ProductCountMapper.INSTANCE.toDTO(productCount));
            }
    }

    @Override
    public CheckResponse addProductToCheck(OperationDto operationDto) {
        Check check = checkService.findCheckById(operationDto.getCheckId());
        if (check.getClosed()) {
            throw new ClosedCheckException();
        }

        operationService.addOperation(operationDto);
        List<ProductForCheckDto> productForCheckDtoList = getProductForCheckDtoList(check);

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

        return CheckResponseToCancellationResponseMapper.INSTANCE.toCancellationResponse(checkResponse);
    }

    @Override
    public ResultResponse closeCancellationCheck(CancellationResponse cancellationResponse) {

        return close(cancellationResponse.getProductList(),
                cancellationResponse.getId(), cancellationResponse.getTotalSum());
    }

    private ProductsReport productsReport(List<Check> checkList) {
        Map<ProductForCheckDto, Integer> productMap = new HashMap<>();
        for (Check check : checkList) {
            List<ProductForCheckDto> productForCheckDtoList = getProductForCheckDtoList(check);
            for (ProductForCheckDto p : productForCheckDtoList) {
                if (productMap.containsKey(p)) {
                    productMap.put(p, productMap.get(p) + p.getCount());
                } else {
                    productMap.put(p, p.getCount());
                }
            }
        }

        for (ProductForCheckDto product : productMap.keySet()) {
            product.setCount(productMap.get(product));
            product.setSum(product.getPrice() * product.getCount());
        }

        Set<ProductForCheckDto> set = productMap.keySet();
        Double totalSum = set.stream()
                .map(ProductForCheckDto::getSum)
                .reduce(Double::sum).get();

        return new ProductsReport(set, totalSum);
    }

    @Override
    public ProductsReport getSaleProductsReport() {
        List<Check> checkList = checkService.findClosedChecks(OperationType.SELLING);
        return productsReport(checkList);
    }

    @Override
    public ProductsReport getGoneProductsReport(String dateFrom, String dateTo) {
        List<Check> checkList = checkService.findClosedChecksByDateBetween(OperationType.SELLING,
                dateFrom, dateTo);
        return productsReport(checkList);
    }

    @Override
    public Map<String, Double> getProceeds() {
        List<Check> checkList = checkService.findClosedChecks(OperationType.SELLING);
        Double totalSum = checkList.stream().map(Check::getTotalSum).reduce(Double::sum).get();
        return Map.of("Proceeds is: ", totalSum);
    }

    @Override
    public CheckResponse removeProductFromCheck(OperationDto operationDto) {
        OperationDto operationDtoRemove = OperationDtoMapper.INSTANCE.map(operationDto);
        operationDtoRemove.setCount(-operationDto.getCount());
        operationDtoRemove.setSum(-operationDto.getSum());

        return addProductToCheck(operationDtoRemove);
    }

    @Override
    public Map<String, Double> getAVGCheckReport() {
        List<Check> checkList = checkService.findClosedChecks(OperationType.SELLING);
        Double totalSum = checkList.stream().map(Check::getTotalSum).reduce(Double::sum).get();
        Integer count = checkList.size();

        return Map.of("Average amount of checks is: ", totalSum / count);
    }

    private List<ProductForCheckDto> getProductForCheckDtoList(Check check) {
        List<Product> productList = check.getOperations().stream()
                .map(Operation::getProduct)
                .collect(Collectors.toList());

        List<ProductForCheckDto> productForCheckDtoList = new ArrayList<>();
        for (Product product : productList) {
            ProductForCheckDto productForCheckDto = ProductToProductForCheckDtoMapper
                    .INSTANCE.toProductForCheckDto(product);

            if (productForCheckDtoList.contains(productForCheckDto)) {
                continue;
            }
            productForCheckDto.setGroupName(product.getGroup().getName());
            Integer amount = operationService.findAmountOfProductByCheckAndProduct(check, product);
            Double sum = product.getPrice() * amount;
            productForCheckDto.setCount(amount);
            productForCheckDto.setSum(sum);

            productForCheckDtoList.add(productForCheckDto);
        }
        return productForCheckDtoList;
    }

    private ResultResponse close(List<ProductForCheckDto> dtoList, Long id, Double totalSum) {
        ResultResponse response;
        List<String> errors = new ArrayList<>();
        for (ProductForCheckDto product : dtoList) {
            Product prod = productService.findProductById(product.getId());
            ProductCount amount = productCountService.findByProduct(prod);
            if (amount.getCount() < product.getCount()) {
                errors.add(product.getProductName() + " - not enough");
            }
        }

        if (errors.isEmpty()) {
            for (ProductForCheckDto product : dtoList) {
                Product prod = productService.findProductById(product.getId());
                ProductCount amount = productCountService.findByProduct(prod);
                amount.setCount(amount.getCount() - product.getCount());
                productCountService.updateProductAmountById(amount.getId(), ProductCountMapper.INSTANCE.toDTO(amount));
                CheckDto checkDto = CheckMapper.INSTANCE.toDTO(checkService.findCheckById(id));
                checkDto.setTotalSum(totalSum);
                checkDto.setDate(new Date());
                checkDto.setClosed(true);
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

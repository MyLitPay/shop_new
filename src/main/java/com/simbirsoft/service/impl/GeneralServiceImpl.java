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

    public void addProductByInvoiceId(Long invoiceId) {
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
                    productMap.put(p, productMap.get(p) + p.getAmount());
                } else {
                    productMap.put(p, p.getAmount());
                }
            }
        }

        for (ProductForCheckDto product : productMap.keySet()) {
            product.setAmount(productMap.get(product));
            product.setSum(product.getPrice() * product.getAmount());
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
            productForCheckDto.setAmount(amount);
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

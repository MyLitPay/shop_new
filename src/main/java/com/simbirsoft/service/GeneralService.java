package com.simbirsoft.service;

import com.simbirsoft.api.dto.OperationDto;
import com.simbirsoft.api.dto.ProductForCheckDto;
import com.simbirsoft.api.response.ProductsReport;
import com.simbirsoft.api.response.CancellationResponse;
import com.simbirsoft.api.response.CheckResponse;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.api.response.SearchProductResponse;

import java.util.Map;

public interface GeneralService {
    void addProductByInvoiceId(Long invoiceId);

    CheckResponse addProductToCheck(OperationDto operationDto);

    ResultResponse closeCheck(CheckResponse checkResponse);

    CancellationResponse addProductForCancellation(OperationDto operationDto);

    ResultResponse closeCancellationCheck(CancellationResponse cancellationResponse);

    SearchProductResponse getProductsByName(String productName);

    SearchProductResponse getProductsByGroup(Long groupId);

    ProductsReport getSaleProductsReport();

    ProductsReport getGoneProductsReport(String dateFrom, String dateTo);

    Map<String, Double> getAVGCheckReport();

    Map<String, Double> getProceeds();

    CheckResponse removeProductFromCheck(OperationDto operationDto);
}

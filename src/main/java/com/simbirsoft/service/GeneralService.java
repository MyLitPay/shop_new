package com.simbirsoft.service;

import com.simbirsoft.api.dto.OperationDto;
import com.simbirsoft.api.response.CancellationResponse;
import com.simbirsoft.api.response.CheckResponse;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.api.response.SearchProductResponse;

public interface GeneralService {
    void addProductByInvoiceId(Long invoiceId);

    CheckResponse addProductToCheck(OperationDto operationDto);

    ResultResponse closeCheck(CheckResponse checkResponse);

    CancellationResponse addProductForCancellation(OperationDto operationDto);

    ResultResponse closeCancellationCheck(CancellationResponse cancellationResponse);

    SearchProductResponse getProductsByName(String productName);

    SearchProductResponse getProductsByGroup(Long groupId);
}

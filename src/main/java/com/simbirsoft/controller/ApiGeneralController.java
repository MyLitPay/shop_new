package com.simbirsoft.controller;

import com.simbirsoft.api.dto.OperationDto;
import com.simbirsoft.api.response.ProductsReport;
import com.simbirsoft.api.response.CancellationResponse;
import com.simbirsoft.api.response.CheckResponse;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.service.GeneralService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {
    final GeneralService generalService;

    public ApiGeneralController(GeneralService generalService) {
        this.generalService = generalService;
    }

    @PostMapping("/product/add")
    public void addProductByInvoiceId(@RequestParam Long invoiceId) {
        generalService.addProductByInvoiceId(invoiceId);
    }

    @PostMapping("/check/add")
    public CheckResponse addProductToCheck(@RequestBody OperationDto operationDto) {
        return generalService.addProductToCheck(operationDto);
    }

    @PostMapping("check/close")
    public ResultResponse closeCheck(@RequestBody CheckResponse checkResponse) {
        return generalService.closeCheck(checkResponse);
    }

    @PostMapping("/cancellation/add")
    public CancellationResponse addProductForCancellation(@RequestBody OperationDto operationDto) {
        return generalService.addProductForCancellation(operationDto);
    }

    @PostMapping("/cancellation/close")
    public ResultResponse closeCancellationCheck(@RequestBody CancellationResponse cancellationResponse) {
        return generalService.closeCancellationCheck(cancellationResponse);
    }

    @GetMapping("/sales")
    public ProductsReport getSalesReport() {
        return generalService.getSaleProductsReport();
    }

    @PostMapping("/gone")
    public ProductsReport getGoneProductsReport(@RequestParam String dateFrom,
                                           @RequestParam String dateTo) {
        return generalService.getGoneProductsReport(dateFrom, dateTo);
    }

    @GetMapping("/avg")
    public Map<String, Double> getAVGCheckReport() {
        return generalService.getAVGCheckReport();
    }

    @GetMapping("/proceeds")
    public Map<String, Double> getProceeds() {
        return generalService.getProceeds();
    }
}

package com.simbirsoft.controller;

import com.simbirsoft.api.dto.OperationDto;
import com.simbirsoft.api.response.CancellationResponse;
import com.simbirsoft.api.response.CheckResponse;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.service.GeneralService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {
    final GeneralService generalService;

    public ApiGeneralController(GeneralService generalService) {
        this.generalService = generalService;
    }

    @PostMapping("/product/add")
    public ResultResponse addProductByInvoiceId(@RequestParam Long invoiceId) {
        return generalService.addProductByInvoiceId(invoiceId);
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
}

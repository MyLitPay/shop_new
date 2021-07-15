package com.simbirsoft.exception.advice;

import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.api.response.ResultResponseType;
import com.simbirsoft.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductAdvice {

    @ExceptionHandler(ProductAmountNotEnoughException.class)
    public ResponseEntity<ResultResponse> handleException(ProductAmountNotEnoughException ex) {
        ResultResponse resultResponse = new ResultResponse(ResultResponseType.ERROR);
        resultResponse.setErrors(ex.getErrors());

        return new ResponseEntity<>(resultResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CancellationNotFoundException.class, CheckNotFoundException.class,
            GroupNotFoundException.class, InvoiceNotFoundException.class,
    OperationNotFoundException.class, ProductAmountNotFoundException.class,
    ProductNotFoundException.class, ClosedCheckException.class, IncorrectDateException.class})
    public ResponseEntity<ResultResponse> handleException(RuntimeException ex) {
        ResultResponse resultResponse = new ResultResponse(ex.getMessage());
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

}

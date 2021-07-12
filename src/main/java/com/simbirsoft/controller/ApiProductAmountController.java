package com.simbirsoft.controller;

import com.simbirsoft.api.dto.ProductAmountDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.service.ProductAmountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amounts")
public class ApiProductAmountController {
    final ProductAmountService productAmountService;

    public ApiProductAmountController(ProductAmountService productAmountService) {
        this.productAmountService = productAmountService;
    }

    @GetMapping
    public List<ProductAmountDto> getAllProductAmounts() {
        return productAmountService.getAllProductAmounts();
    }

    @PostMapping
    public ProductAmountDto addProductAmount(@RequestBody ProductAmountDto dto) {
        return productAmountService.addProductAmount(dto);
    }

    @PutMapping
    public List<ProductAmountDto> updateAllProductAmounts(@RequestBody List<ProductAmountDto> request) {
        return productAmountService.updateAllProductAmounts(request);
    }

    @DeleteMapping
    public ResultResponse deleteAllProductAmounts() {
        return productAmountService.deleteAllProductAmounts();
    }

    @GetMapping("/{id}")
    public ProductAmountDto getProductAmountById(@PathVariable("id") Long id) {
        return productAmountService.getProductAmountById(id);
    }

    @PutMapping("/{id}")
    public ProductAmountDto updateProductAmountById(@PathVariable("id") Long id,
                                     @RequestBody ProductAmountDto dto) {
        return productAmountService.updateProductAmountById(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResultResponse deleteProductAmountById(@PathVariable("id") Long id) {
        return productAmountService.deleteProductAmountById(id);
    }

}

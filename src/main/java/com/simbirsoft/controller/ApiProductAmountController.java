package com.simbirsoft.controller;

import com.simbirsoft.api.dto.ProductCountDto;
import com.simbirsoft.service.ProductCountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amounts")
public class ApiProductAmountController {
    final ProductCountService productCountService;

    public ApiProductAmountController(ProductCountService productCountService) {
        this.productCountService = productCountService;
    }

    @GetMapping
    public List<ProductCountDto> getAllProductAmounts() {
        return productCountService.getAllProductAmounts();
    }

    @PostMapping
    public ProductCountDto addProductAmount(@RequestBody ProductCountDto dto) {
        return productCountService.addProductAmount(dto);
    }

    @PutMapping
    public List<ProductCountDto> updateAllProductAmounts(@RequestBody List<ProductCountDto> request) {
        return productCountService.updateAllProductAmounts(request);
    }

    @DeleteMapping
    public void deleteAllProductAmounts() {
        productCountService.deleteAllProductAmounts();
    }

    @GetMapping("/{id}")
    public ProductCountDto getProductAmountById(@PathVariable("id") Long id) {
        return productCountService.getProductAmountById(id);
    }

    @PutMapping("/{id}")
    public ProductCountDto updateProductAmountById(@PathVariable("id") Long id,
                                                   @RequestBody ProductCountDto dto) {
        return productCountService.updateProductAmountById(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteProductAmountById(@PathVariable("id") Long id) {
        productCountService.deleteProductAmountById(id);
    }

}

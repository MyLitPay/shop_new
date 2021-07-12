package com.simbirsoft.controller;

import com.simbirsoft.api.dto.ProductDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ApiProductController {
    final ProductService productService;

    public ApiProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public ProductDto addProduct(@RequestBody ProductDto dto) {
        return productService.addProduct(dto);
    }

    @PutMapping
    public List<ProductDto> updateAllProducts(@RequestBody List<ProductDto> request) {
        return productService.updateAllProducts(request);
    }

    @DeleteMapping
    public ResultResponse deleteAllProducts() {
        return productService.deleteAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable("id") Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public ProductDto updateProductById(@PathVariable("id") Long id,
                                 @RequestBody ProductDto dto) {
        return productService.updateProductById(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResultResponse deleteProductById(@PathVariable("id") Long id) {
        return productService.deleteProductById(id);
    }

//    @GetMapping
//    public List<ProductDto> getProducts() {
//        return productService.getProducts();
//    }
//
//    @PostMapping
//    public ResultResponse addProduct(@RequestBody InvoiceDto invoiceDto) {
//        return productService.addProduct(invoiceDto);
//    }
}

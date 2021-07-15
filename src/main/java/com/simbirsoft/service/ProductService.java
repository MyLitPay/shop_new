package com.simbirsoft.service;

import com.simbirsoft.api.dto.ProductDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.model.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto addProduct(ProductDto dto);
    List<ProductDto> updateAllProducts(List<ProductDto> request);
    void deleteAllProducts();
    ProductDto getProductById(Long id);
    ProductDto updateProductById(Long id, ProductDto dto);
    void deleteProductById(Long id);

    Product findProductById(long id);
    Product findByNameAndPrice(String name, Double price);

//    List<ProductDto> getProducts();
//    ResultResponse addProduct(InvoiceDto invoiceDto);
}

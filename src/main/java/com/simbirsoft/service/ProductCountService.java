package com.simbirsoft.service;

import com.simbirsoft.api.dto.ProductCountDto;
import com.simbirsoft.model.Product;
import com.simbirsoft.model.ProductCount;

import java.util.List;

public interface ProductCountService {
    List<ProductCountDto> getAllProductAmounts();
    ProductCountDto addProductAmount(ProductCountDto dto);
    List<ProductCountDto> updateAllProductAmounts(List<ProductCountDto> request);
    void deleteAllProductAmounts();
    ProductCountDto getProductAmountById(Long id);
    ProductCountDto updateProductAmountById(Long id, ProductCountDto dto);
    void deleteProductAmountById(Long id);

    ProductCount findProductAmountById(long id);
    ProductCount findByProduct(Product product);
}

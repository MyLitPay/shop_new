package com.simbirsoft.service;

import com.simbirsoft.api.dto.ProductAmountDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.model.Product;
import com.simbirsoft.model.ProductAmount;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductAmountService {
    List<ProductAmountDto> getAllProductAmounts();
    ProductAmountDto addProductAmount(ProductAmountDto dto);
    List<ProductAmountDto> updateAllProductAmounts(List<ProductAmountDto> request);
    ResultResponse deleteAllProductAmounts();
    ProductAmountDto getProductAmountById(Long id);
    ProductAmountDto updateProductAmountById(Long id, ProductAmountDto dto);
    void deleteProductAmountById(Long id);

    ProductAmount findProductAmountById(long id);
    ProductAmount findByProduct(Product product);
}

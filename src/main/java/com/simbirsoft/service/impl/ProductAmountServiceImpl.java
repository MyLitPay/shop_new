package com.simbirsoft.service.impl;

import com.simbirsoft.api.dto.ProductAmountDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.api.response.ResultResponseType;
import com.simbirsoft.exception.ProductAmountNotFoundException;
import com.simbirsoft.exception.ProductNotFoundException;
import com.simbirsoft.mapper.ProductAmountMapper;
import com.simbirsoft.model.Product;
import com.simbirsoft.model.ProductAmount;
import com.simbirsoft.repo.ProductAmountRepository;
import com.simbirsoft.repo.ProductRepository;
import com.simbirsoft.service.ProductAmountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductAmountServiceImpl implements ProductAmountService {
    final ProductAmountRepository productAmountRepository;
    final ProductRepository productRepository;

    public ProductAmountServiceImpl(ProductAmountRepository productAmountRepository, ProductRepository productRepository) {
        this.productAmountRepository = productAmountRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ResponseEntity<List<ProductAmountDto>> getAllProductAmounts() {
        List<ProductAmountDto> list = productAmountRepository.findAll().stream()
                .map(ProductAmountMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            return new ResponseEntity<>(list, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    public ProductAmountDto addProductAmount(ProductAmountDto dto) {
        ProductAmount productAmount = ProductAmountMapper.INSTANCE.toEntity(dto);
        ProductAmount d = setConstraints(dto, productAmount);
        ProductAmount prodAmountFromDB = productAmountRepository.saveAndFlush(d);
        return ProductAmountMapper.INSTANCE.toDTO(prodAmountFromDB);
    }

    @Override
    public ResponseEntity<List<ProductAmountDto>> updateAllProductAmounts(List<ProductAmountDto> request) {
        List<ProductAmount> productAmountList = new ArrayList<>();

        for (ProductAmountDto dto : request) {
            ProductAmount productAmount = updateProductAmountData(findProductAmountById(dto.getProdId()), dto);
            productAmountList.add(productAmount);
        }
        productAmountRepository.saveAll(productAmountList);
        return getAllProductAmounts();
    }

    @Override
    public ResultResponse deleteAllProductAmounts() {
        try {
            List<ProductAmount> productAmounts = productAmountRepository.findAll();
            for (ProductAmount amounts : productAmounts) {
                deleteConstraints(amounts);
            }
            productAmountRepository.deleteAll(productAmounts);
            return new ResultResponse(ResultResponseType.OK);
        } catch (Exception ex) {
            return new ResultResponse(ResultResponseType.ERROR);
        }
    }

    @Override
    public ProductAmountDto getProductAmountById(Long id) {
        ProductAmount amount = findProductAmountById(id);
        System.out.println(amount.getId());
        System.out.println(amount.getAmount());
        System.out.println(amount.getProduct().getId());
        return ProductAmountMapper.INSTANCE.toDTO(amount);
    }

    @Override
    public ProductAmountDto updateProductAmountById(Long id, ProductAmountDto dto) {
        ProductAmount amount = updateProductAmountData(findProductAmountById(id), dto);
        ProductAmount productAmountFromDB = productAmountRepository.saveAndFlush(amount);
        return ProductAmountMapper.INSTANCE.toDTO(productAmountFromDB);
    }

    @Override
    public void deleteProductAmountById(Long id) {
            deleteConstraints(findProductAmountById(id));
            productAmountRepository.deleteById(id);
    }

    public ProductAmount findProductAmountById(long id) {
        return productAmountRepository.findById(id)
                .orElseThrow(() -> new ProductAmountNotFoundException("ProductAmount not found"));
    }

    public ProductAmount findByProduct(Product product) {
        return productAmountRepository.findByProduct(product)
                .orElseThrow(() -> new ProductAmountNotFoundException("Amount not found"));
    }

    private ProductAmount updateProductAmountData(ProductAmount productAmount, ProductAmountDto dto) {
        productAmount.setAmount(dto.getAmount());
        return setConstraints(dto, productAmount);
    }

    private ProductAmount setConstraints(ProductAmountDto dto, ProductAmount amount) {
        if (dto.getProdId() != null) {
            Product product = getProductFromDB(dto.getProdId());
            amount.setProduct(product);
        }
        return amount;
    }

    private void deleteConstraints(ProductAmount amount) {
        if (amount.getProduct() != null) {
            Product product = getProductFromDB(amount.getProduct().getId());
        }
    }

    private Product getProductFromDB(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }
}

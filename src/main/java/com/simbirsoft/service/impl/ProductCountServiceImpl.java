package com.simbirsoft.service.impl;

import com.simbirsoft.api.dto.ProductCountDto;
import com.simbirsoft.exception.ProductAmountNotFoundException;
import com.simbirsoft.exception.ProductNotFoundException;
import com.simbirsoft.mapper.ProductCountMapper;
import com.simbirsoft.model.Product;
import com.simbirsoft.model.ProductCount;
import com.simbirsoft.repo.ProductCountRepository;
import com.simbirsoft.repo.ProductRepository;
import com.simbirsoft.service.ProductCountService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCountServiceImpl implements ProductCountService {
    final ProductCountRepository productCountRepository;
    final ProductRepository productRepository;

    public ProductCountServiceImpl(ProductCountRepository productCountRepository, ProductRepository productRepository) {
        this.productCountRepository = productCountRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductCountDto> getAllProductAmounts() {
        List<ProductCountDto> list = productCountRepository.findAll().stream()
                .map(ProductCountMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            throw new ProductAmountNotFoundException();
        }
        return list;
    }

    @Override
    public ProductCountDto addProductAmount(ProductCountDto dto) {
        ProductCount productCount = ProductCountMapper.INSTANCE.toEntity(dto);
        ProductCount d = setConstraints(dto, productCount);
        ProductCount prodAmountFromDB = productCountRepository.saveAndFlush(d);
        return ProductCountMapper.INSTANCE.toDTO(prodAmountFromDB);
    }

    @Override
    public List<ProductCountDto> updateAllProductAmounts(List<ProductCountDto> request) {
        List<ProductCount> productCountList = new ArrayList<>();

        for (ProductCountDto dto : request) {
            ProductCount productCount = updateProductAmountData(findProductAmountById(dto.getProdId()), dto);
            productCountList.add(productCount);
        }
        productCountRepository.saveAll(productCountList);
        return getAllProductAmounts();
    }

    @Override
    public void deleteAllProductAmounts() {
            List<ProductCount> productCounts = productCountRepository.findAll();
            for (ProductCount amounts : productCounts) {
                deleteConstraints(amounts);
            }
            productCountRepository.deleteAll(productCounts);
        if (productCounts.isEmpty()) {
            throw new ProductAmountNotFoundException();
        }
    }

    @Override
    public ProductCountDto getProductAmountById(Long id) {
        ProductCount amount = findProductAmountById(id);
        System.out.println(amount.getId());
        System.out.println(amount.getCount());
        System.out.println(amount.getProduct().getId());
        return ProductCountMapper.INSTANCE.toDTO(amount);
    }

    @Override
    public ProductCountDto updateProductAmountById(Long id, ProductCountDto dto) {
        ProductCount amount = updateProductAmountData(findProductAmountById(id), dto);
        ProductCount productCountFromDB = productCountRepository.saveAndFlush(amount);
        return ProductCountMapper.INSTANCE.toDTO(productCountFromDB);
    }

    @Override
    public void deleteProductAmountById(Long id) {
            deleteConstraints(findProductAmountById(id));
            productCountRepository.deleteById(id);
    }

    public ProductCount findProductAmountById(long id) {
        return productCountRepository.findById(id)
                .orElseThrow(ProductAmountNotFoundException::new);
    }

    public ProductCount findByProduct(Product product) {
        return productCountRepository.findByProduct(product)
                .orElseThrow(ProductAmountNotFoundException::new);
    }

    private ProductCount updateProductAmountData(ProductCount productCount, ProductCountDto dto) {
        productCount.setCount(dto.getCount());
        return setConstraints(dto, productCount);
    }

    private ProductCount setConstraints(ProductCountDto dto, ProductCount amount) {
        if (dto.getProdId() != null) {
            Product product = getProductFromDB(dto.getProdId());
            amount.setProduct(product);
        }
        return amount;
    }

    private void deleteConstraints(ProductCount amount) {
        if (amount.getProduct() != null) {
            Product product = getProductFromDB(amount.getProduct().getId());
        }
    }

    private Product getProductFromDB(Long id) {
        return productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }
}

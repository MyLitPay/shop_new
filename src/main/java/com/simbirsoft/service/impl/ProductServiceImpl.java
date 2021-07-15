package com.simbirsoft.service.impl;

import com.simbirsoft.api.dto.ProductDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.api.response.ResultResponseType;
import com.simbirsoft.exception.CancellationNotFoundException;
import com.simbirsoft.exception.CheckNotFoundException;
import com.simbirsoft.exception.GroupNotFoundException;
import com.simbirsoft.exception.ProductNotFoundException;
import com.simbirsoft.mapper.ProductMapper;
import com.simbirsoft.model.Group;
import com.simbirsoft.model.Product;
import com.simbirsoft.repo.GroupRepository;
import com.simbirsoft.repo.ProductRepository;
import com.simbirsoft.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    final ProductRepository productRepository;
    final GroupRepository groupRepository;

    public ProductServiceImpl(ProductRepository productRepository, GroupRepository groupRepository) {
        this.productRepository = productRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<ProductDto> list = productRepository.findAll().stream()
                .map(ProductMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            throw new ProductNotFoundException();
        }
        return list;
    }

    @Override
    public ProductDto addProduct(ProductDto dto) {
        Product product = ProductMapper.INSTANCE.toEntity(dto);
        Product prodFromDB = productRepository.saveAndFlush(setConstraints(dto, product));
        return ProductMapper.INSTANCE.toDTO(prodFromDB);
    }

    @Override
    public List<ProductDto> updateAllProducts(List<ProductDto> request) {
        List<Product> productList = new ArrayList<>();

        for (ProductDto dto : request) {
            Product product = updateProductData(findProductById(dto.getId()), dto);
            productList.add(product);
        }
        productRepository.saveAll(productList);
        return getAllProducts();
    }

    @Override
    public void deleteAllProducts() {
            List<Product> products = productRepository.findAll();
            for (Product product : products) {
                deleteConstraints(product);
            }
            productRepository.deleteAll(products);
        if (products.isEmpty()) {
            throw new ProductNotFoundException();
        }
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = findProductById(id);
        return ProductMapper.INSTANCE.toDTO(product);
    }

    @Override
    public ProductDto updateProductById(Long id, ProductDto dto) {
        Product product = updateProductData(findProductById(id), dto);
        Product productFromDB = productRepository.saveAndFlush(product);
        return ProductMapper.INSTANCE.toDTO(productFromDB);
    }

    @Override
    public void deleteProductById(Long id) {
            deleteConstraints(findProductById(id));
            productRepository.deleteById(id);
    }

    public Product findProductById(long id) {
        return productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    public Product findByNameAndPrice(String name, Double price) {
        return productRepository.findByNameAndPrice(name, price)
                .orElse(null);
    }

    private Product updateProductData(Product product, ProductDto dto) {
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        return setConstraints(dto, product);
    }

    private Product setConstraints(ProductDto dto, Product product) {
        if (dto.getGroupId() != null) {
            Group group = getGroupFromDB(dto.getGroupId());
            product.setGroup(group);
            if (group.getProducts() == null) {
                group.setProducts(new ArrayList<>());
            }
            group.getProducts().add(product);
        }
        return product;
    }

    private void deleteConstraints(Product product) {
        if (product.getGroup() != null) {
            Group group = getGroupFromDB(product.getGroup().getId());
            group.getProducts().remove(product);
        }
    }

    private Group getGroupFromDB(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(GroupNotFoundException::new);
    }
}

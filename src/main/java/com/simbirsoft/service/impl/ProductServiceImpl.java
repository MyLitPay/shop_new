package com.simbirsoft.service.impl;

import com.simbirsoft.api.dto.ProductDto;
import com.simbirsoft.api.response.ResultResponse;
import com.simbirsoft.api.response.ResultResponseType;
import com.simbirsoft.exception.GroupNotFoundException;
import com.simbirsoft.exception.ProductNotFoundException;
import com.simbirsoft.mapper.ProductMapper;
import com.simbirsoft.model.Group;
import com.simbirsoft.model.Product;
import com.simbirsoft.repo.GroupRepository;
import com.simbirsoft.repo.ProductRepository;
import com.simbirsoft.service.ProductService;
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
        return productRepository.findAll().stream()
                .map(ProductMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
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
    public ResultResponse deleteAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            for (Product product : products) {
                deleteConstraints(product);
            }
            productRepository.deleteAll(products);
            return new ResultResponse(ResultResponseType.OK);
        } catch (Exception ex) {
            return new ResultResponse(ResultResponseType.ERROR);
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
    public ResultResponse deleteProductById(Long id) {
        try {
            deleteConstraints(productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found")));
            productRepository.deleteById(id);
            return new ResultResponse(ResultResponseType.OK);
        } catch (Exception ex) {
            return new ResultResponse(ResultResponseType.ERROR);
        }
    }

    public Product findProductById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
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
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));
    }

//    @Override
//    public List<ProductDto> getProducts() {
////        return getProductDTOList(productRepository.findAll());
//        return null;
//    }
//
//    @Override
//    public ResultResponse addProduct(InvoiceDto invoiceDto) {
//        try {
//            Invoice invoice = new Invoice();
//            invoice.setProductName(invoiceDto.getName());
//            invoice.setAmount(invoiceDto.getAmount());
//            invoice.setPrice(invoiceDto.getPrice());
//            invoice.setSum(invoice.getSum());
//            invoiceRepository.saveAndFlush(invoice);
//
//            Product dbProduct = productRepository
//                    .findByNameAndPrice(invoice.getProductName(), invoice.getPrice())
//                    .orElse(null);
//
//            Group dbGroup = groupRepository.
//                    findByName(invoiceDto.getGroupName())
//                    .orElse(null);
//
//            Product product;
//            Group group;
//            ProductAmount productAmount;
//
//            if (dbProduct == null) {
//                product = new Product();
//                product.setName(invoice.getProductName());
//                product.setPrice(invoice.getPrice());
//
//                if (dbGroup == null) {
//                    group = new Group();
//                    group.setName(invoiceDto.getName());
//
//                    product.setGroup(group);
//                    group.setProducts(new ArrayList<>());
//                    group.getProducts().add(product);
//                    groupRepository.saveAndFlush(group);
//                } else {
//                    product.setGroup(dbGroup);
//                    dbGroup.getProducts().add(product);
//                    groupRepository.saveAndFlush(dbGroup);
//                }
//
//                productAmount = new ProductAmount();
//                productAmount.setProduct(product);
//                productAmount.setAmount(invoice.getAmount());
//                productAmountRepository.saveAndFlush(productAmount);
//                productRepository.saveAndFlush(product);
//
//            } else {
//                ProductAmount dbProductAmount = productAmountRepository
//                        .findById(dbProduct.getId())
//                        .orElseThrow(() -> new ProductAmountNotFoundException("Amount not found"));
//                dbProductAmount.setAmount(dbProductAmount.getAmount() + invoice.getAmount());
//                productAmountRepository.saveAndFlush(dbProductAmount);
//            }
//
//            return new ResultResponse(true);
//
//        } catch (Exception ex) {
//            return new ResultResponse(false);
//        }
//    }
//
//    private List<ProductDto> getProductDTOList(List<Product> products) {
//        return products.stream()
//                .map(ProductDto::new)
//                .collect(Collectors.toList());
//    }
}

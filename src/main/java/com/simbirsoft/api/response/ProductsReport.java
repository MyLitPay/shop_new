package com.simbirsoft.api.response;

import com.simbirsoft.api.dto.ProductForCheckDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class ProductsReport {
    private Set<ProductForCheckDto> products;
    private Double totalSum;

    public ProductsReport(Set<ProductForCheckDto> products, Double totalSum) {
        this.products = products;
        this.totalSum = totalSum;
    }
}

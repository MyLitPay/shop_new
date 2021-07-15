package com.simbirsoft.api.response;

import com.simbirsoft.api.dto.ProductDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SearchProductResponse {
    private List<ProductDto> products;

    public SearchProductResponse(List<ProductDto> products) {
        this.products = products;
    }
}

package com.simbirsoft.api.response;

import com.simbirsoft.api.dto.ProductForCheckDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CheckResponse {
    private Long id;
    private String description;
    private List<ProductForCheckDto> productList;
    private Double totalSum;
    private String date;
}

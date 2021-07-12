package com.simbirsoft.api.response;

import com.simbirsoft.api.dto.ProductForCheckDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CancellationResponse {
    private Long id;
    private List<ProductForCheckDto> productList;
    private String reason;
    private String date;
    private Double totalSum;

}

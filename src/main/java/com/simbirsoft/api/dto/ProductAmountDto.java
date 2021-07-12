package com.simbirsoft.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductAmountDto {
    private Long id;
    private Long prodId;
    private Integer amount;
}

package com.simbirsoft.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvoiceDto {
    private Long id;
    private String name;
    private Integer amount;
    private Double price;
    private Double sum;
    private Long groupId;
}

package com.simbirsoft.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OperationDto {
    private Long id;
    private String operation;
    private Integer count;
    private Double sum;
    private Long prodId;
    private Long checkId;
}

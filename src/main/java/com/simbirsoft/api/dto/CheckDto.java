package com.simbirsoft.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class CheckDto {
    private Long id;
    private String description;
    private Double totalSum;
    private Date date;
}

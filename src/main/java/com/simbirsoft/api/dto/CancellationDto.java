package com.simbirsoft.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class CancellationDto {
    private Long id;
    private String reason;
    private Date date;
}

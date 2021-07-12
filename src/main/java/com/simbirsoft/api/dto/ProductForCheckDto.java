package com.simbirsoft.api.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"productName", "price"})
public class ProductForCheckDto {
    private Long id;
    private String productName;
    private Double price;
    private String groupName;
    private Integer amount;
    private Double sum;

    public ProductForCheckDto(String productName, Double price) {
        this.productName = productName;
        this.price = price;
    }
}

package com.simbirsoft.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter @Setter
@NoArgsConstructor
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

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(productName);
        hcb.append(price);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProductForCheckDto)) {
            return false;
        }
        ProductForCheckDto that = (ProductForCheckDto) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(productName, that.productName);
        eb.append(price, that.price);
        return eb.isEquals();
    }
}

package com.simbirsoft.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "prod_amount")
@NoArgsConstructor
@Getter @Setter
public class ProductAmount {

    @Id
    @Column(name = "prod_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "prod_id")
    private Product product;

    private Integer amount;
}

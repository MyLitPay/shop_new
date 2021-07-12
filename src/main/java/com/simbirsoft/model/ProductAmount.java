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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_id")
    private Product product;

    private Integer amount;
}

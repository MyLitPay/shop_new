package com.simbirsoft.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "operations")
@Getter @Setter
@NoArgsConstructor
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private OperationsEnum operation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prod_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "prod_amount")
    private Integer amount;

    private Double sum;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "check_id")
    private Check check;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "cancellation_id")
    private Cancellation cancellation;

    public String getNameOfOperation() {
        String nameOfOperation;
        switch (operation.getNumOfOperation()) {
            case 0: nameOfOperation = OperationsEnum.SUPPLY.toString();
                break;
            case 1: nameOfOperation = OperationsEnum.SELLING.toString();
                break;
            case 2: nameOfOperation = OperationsEnum.RESCUE.toString();
                break;
            default: nameOfOperation = "unknown operation";
                break;
        }
        return nameOfOperation;
    }
}

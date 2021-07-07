package com.simbirsoft.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
@NoArgsConstructor
@Getter @Setter
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "group_name")
    private String name;

    @OneToMany(mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
        product.setGroup(this);
    }

    public void deleteProduct(Product product) {
        products.remove(product);
        product.setGroup(null);
    }
}

package com.simbirsoft.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "checks")
@NoArgsConstructor
@Getter @Setter
public class Check {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String description;

    private Boolean closed;

    @OneToMany(mappedBy = "check",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Operation> operations;

    private Double totalSum;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Check(Long id) {
        this.id = id;
    }

    public Check(Long id, String description, Boolean closed, Double totalSum, Date date) {
        this.id = id;
        this.description = description;
        this.closed = closed;
        this.totalSum = totalSum;
        this.date = date;
    }
}

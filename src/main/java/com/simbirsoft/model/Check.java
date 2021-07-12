package com.simbirsoft.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "check",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Operation> operations;

    private Double totalSum;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
}

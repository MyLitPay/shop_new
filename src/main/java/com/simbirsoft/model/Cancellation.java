package com.simbirsoft.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cancellations")
@NoArgsConstructor
@Getter @Setter
public class Cancellation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String reason;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @OneToMany(mappedBy = "cancellation",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Operation> operations = new ArrayList<>();
}

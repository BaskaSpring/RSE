package com.baska.RSE.Models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "table_types")
public class Types {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "priority")
    private int priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EType EType;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "value_enum",
            joinColumns = @JoinColumn(name = "types_id"),
            inverseJoinColumns = @JoinColumn(name = "enum_id")
    )
    private EnumValues enumValue;

}

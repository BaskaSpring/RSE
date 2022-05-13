package com.baska.RSE.Models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "enum_value")
public class EnumValues {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @ElementCollection
    @CollectionTable(name="enum_values", joinColumns=@JoinColumn(name="enum_id"))
    @Column(name="values")
    private List<String> enumTypes;
}

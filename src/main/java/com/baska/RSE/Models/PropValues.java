package com.baska.RSE.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "prop_value")
public class PropValues {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection(fetch= FetchType.EAGER)
    @CollectionTable(name = "prop_values")
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    Map<Long, String> propValues = new HashMap<>();
}

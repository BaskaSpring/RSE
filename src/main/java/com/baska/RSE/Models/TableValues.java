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
@Table(name = "table_value")
public class TableValues {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer row;

    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name = "row_values")
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    Map<Long, String> rowValues = new HashMap<>();

}

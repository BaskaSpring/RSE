package com.baska.RSE.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "table_column_and_boolean")
public class TableColumnAndBoolean {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "table_column_id")
    private String tableColumnId;

    @Column(name = "boolean_value")
    private boolean value;


}

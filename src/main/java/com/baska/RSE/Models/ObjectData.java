package com.baska.RSE.Models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "objects")
public class ObjectData {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name =  "id", columnDefinition = "VARCHAR(50)",updatable = false,nullable = false)
    private String id;

    private boolean enabled;

    private String customTableId;

    private String userId;

    private Instant timestamp =Instant.now();

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "prop_values_table",
            joinColumns = @JoinColumn(name = "objects_id"),
            inverseJoinColumns = @JoinColumn(name = "strings_id")
    )
    private PropValues propValues;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "values_table",
            joinColumns = @JoinColumn(name = "objects_id"),
            inverseJoinColumns = @JoinColumn(name = "strings_id")
    )
    private List<TableValues> values;



}

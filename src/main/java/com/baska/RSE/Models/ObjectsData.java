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
@Table(name = "objects_data")
public class ObjectsData {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name =  "id", columnDefinition = "VARCHAR(50)",updatable = false,nullable = false)
    private String id;

    private String objectId;

    private String userId;

    private Instant timestamp = Instant.now();

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinTable(
            name = "numbers_table",
            joinColumns = @JoinColumn(name = "objects_id"),
            inverseJoinColumns = @JoinColumn(name = "numbers_id")
    )
    private List<TableColumnAndNumberTable> numbers;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinTable(
            name = "strings_table",
            joinColumns = @JoinColumn(name = "objects_id"),
            inverseJoinColumns = @JoinColumn(name = "strings_id")
    )
    private List<TableColumnAndStringTable> strings;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinTable(
            name = "booleans_table",
            joinColumns = @JoinColumn(name = "objects_id"),
            inverseJoinColumns = @JoinColumn(name = "booleans_id")
    )
    private List<TableColumnAndBoolean> booleans;



}

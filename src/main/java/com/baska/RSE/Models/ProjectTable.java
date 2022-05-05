package com.baska.RSE.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projects_table")
public class ProjectTable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name =  "id", columnDefinition = "VARCHAR(50)",updatable = false,nullable = false)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "timestamp")
    private Instant timestamp =Instant.now();

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "projects_table_roles",
            joinColumns = @JoinColumn(name = "projects_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "project_tables_colums",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "column_id")
    )
    private Set<TableColumn> columns;

}

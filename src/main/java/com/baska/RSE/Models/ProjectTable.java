package com.baska.RSE.Models;

import com.baska.RSE.DAO.TableComparator;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;
import java.util.TreeSet;

@Data
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

    public ProjectTable() {
    }

    public ProjectTable(String id, String name, Boolean enabled, Instant timestamp, Set<Role> roles, Set<TableColumn> columns) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
        this.timestamp = timestamp;
        this.roles = roles;
        this.columns = columns;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<TableColumn> getColumns() {
        Set<TableColumn> sorted = new TreeSet<>(new TableComparator());
        sorted.addAll(columns);
        return sorted;
    }

    public void setColumns(Set<TableColumn> columns) {
        this.columns = columns;
    }
}

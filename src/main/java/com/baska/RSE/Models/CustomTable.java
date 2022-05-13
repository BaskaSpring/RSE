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
@Table(name = "custom_table")
public class CustomTable {

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
            name = "custom_table_roles",
            joinColumns = @JoinColumn(name = "table_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "custom_tables_colums",
            joinColumns = @JoinColumn(name = "table_id"),
            inverseJoinColumns = @JoinColumn(name = "column_id")
    )
    private Set<Types> columns;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "custom_tables_props",
            joinColumns = @JoinColumn(name = "table_id"),
            inverseJoinColumns = @JoinColumn(name = "props_id")
    )
    private Set<Types> props;

    public CustomTable() {
    }

    public CustomTable(String id, String name, Boolean enabled, Instant timestamp, Set<Role> roles, Set<Types> columns, Set<Types> props) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
        this.timestamp = timestamp;
        this.roles = roles;
        this.columns = columns;
        this.props = props;
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

    public Set<Types> getColumns() {
        Set<Types> sorted = new TreeSet<>(new TableComparator());
        sorted.addAll(columns);
        return sorted;
    }

    public void setColumns(Set<Types> columns) {
        this.columns = columns;
    }

    public Set<Types> getProps() {
        Set<Types> sorted = new TreeSet<>(new TableComparator());
        sorted.addAll(props);
        return sorted;
    }

    public void setProps(Set<Types> props) {
        this.props = props;
    }
}

package com.baska.RSE.Models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "banned")
public class Banned {
    @Id
    private String ip;
    @Column(name="count")
    private Integer count;
    @Column(name="timestamp")
    private Instant timestamp;
}

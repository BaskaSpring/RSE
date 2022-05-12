package com.baska.RSE.Repositories;

import com.baska.RSE.Models.TableValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableValuesRepository extends JpaRepository<TableValues,Long> {



}

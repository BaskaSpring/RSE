package com.baska.RSE.Repositories;

import com.baska.RSE.Models.ProjectTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectTableRepository extends JpaRepository<ProjectTable,String> {

    @Query("select x from ProjectTable as x where x.name =:name")
    Optional<ProjectTable> findByName(@Param("name")String name);


    @Query("select x from ProjectTable as x where x.id =:id")
    Optional<ProjectTable> findById(@Param("id")String id);
}

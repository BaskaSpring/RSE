package com.baska.RSE.Repositories;

import com.baska.RSE.Models.CustomTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomTableRepository extends JpaRepository<CustomTable,String> {

    @Query("select x from CustomTable as x where x.name =:name")
    Optional<CustomTable> findByName(@Param("name")String name);


    @Query("select x from CustomTable as x where x.id =:id")
    Optional<CustomTable> findById(@Param("id")String id);


    @Query("select x from CustomTable as x where x.enabled=:enabled")
    List<CustomTable> getEnabledProjects(@Param("enabled")Boolean enabled);


    @Query("select x from CustomTable  as x")
    List<CustomTable> getAllTables();
}

package com.baska.RSE.Repositories;

import com.baska.RSE.Models.Types;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableColumnRepository extends JpaRepository<Types,Long> {


    @Query("select x from Types as x where x.id =:id")
    Optional<Types> findById(@Param("id")Long id);
}

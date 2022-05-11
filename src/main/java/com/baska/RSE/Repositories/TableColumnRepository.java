package com.baska.RSE.Repositories;

import com.baska.RSE.Models.TableColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableColumnRepository extends JpaRepository<TableColumn,Long> {


    @Query("select x from TableColumn as x where x.id =:id")
    Optional<TableColumn> findById(@Param("id")Long id);
}

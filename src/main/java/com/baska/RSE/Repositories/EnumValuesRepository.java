package com.baska.RSE.Repositories;

import com.baska.RSE.Models.EnumValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnumValuesRepository extends JpaRepository<EnumValues,Long> {

    @Query("select x from EnumValues as x")
    List<EnumValues> getAll();


    @Query("select x from EnumValues as x where x.id =:id")
    Optional<EnumValues> findById(@Param("id") Long id);

}

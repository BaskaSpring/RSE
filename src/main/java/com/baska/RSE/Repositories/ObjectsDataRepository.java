package com.baska.RSE.Repositories;

import com.baska.RSE.Models.ObjectData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ObjectsDataRepository extends JpaRepository<ObjectData,String> {


    @Query("select x from ObjectData as x where x.id=:id")
    Optional<ObjectData> getObjectsById(@Param("id") String id);
}

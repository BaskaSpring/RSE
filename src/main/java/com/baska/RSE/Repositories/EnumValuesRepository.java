package com.baska.RSE.Repositories;

import com.baska.RSE.Models.EnumValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnumValuesRepository extends JpaRepository<EnumValues,Long> {


}

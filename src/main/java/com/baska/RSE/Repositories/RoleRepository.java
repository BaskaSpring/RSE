package com.baska.RSE.Repositories;

import com.baska.RSE.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {

    @Query("select x from Role as x where  x.enabled=true")
    List<Role> findAll();

    @Query("select x from Role as x where x.id = :id")
    Optional<Role> findRoleById(@Param("id") Long  id);

}

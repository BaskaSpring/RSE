package com.baska.RSE.Repositories;

import com.baska.RSE.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    Role findByRole(Role role);
}

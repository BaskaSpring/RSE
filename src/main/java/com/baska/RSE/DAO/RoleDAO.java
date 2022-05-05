package com.baska.RSE.DAO;


import com.baska.RSE.Models.Role;
import com.baska.RSE.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleDAO {

    @Autowired
    RoleRepository roleRepository;

    public List<Role> getRoles(){
        List<Role> roles = roleRepository.findAll();
        return roles;
    }


    public Role getRoleById(Long id){
        return roleRepository.findRoleById(id).orElse(null);
    }
}

package com.baska.RSE.DAO;

import com.baska.RSE.Models.Role;
import com.baska.RSE.Models.User;
import com.baska.RSE.Repositories.RoleRepository;
import com.baska.RSE.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserDAO {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public Set<Role> getRoles(String userName){
        User user = userRepository.findByUserName(userName);
        return user.getRoles();
    }
}

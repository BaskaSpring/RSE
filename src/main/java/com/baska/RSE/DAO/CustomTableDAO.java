package com.baska.RSE.DAO;

import com.baska.RSE.Models.CustomTable;
import com.baska.RSE.Models.Role;
import com.baska.RSE.Payload.Constructor.CustomTablePayload;
import com.baska.RSE.Repositories.CustomTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CustomTableDAO {

    @Autowired
    CustomTableRepository customTableRepository;

    public boolean isPresentByName(String name){
        if (name==null)
            return false;
        return customTableRepository.findByName(name).isPresent();
    }


    public List<CustomTable> getProjectList(){
        return customTableRepository.getAllProjects();
    }

    public Set<CustomTable> getProjectsByRoles(Set<Role> roles){
        List<CustomTable> customTables = customTableRepository.getEnabledProjects(true);
        Set<CustomTable> response = new HashSet<>();
        for (CustomTable obj: customTables){
            Set<Role> rolesObj = obj.getRoles();
            for (Role roleObj :rolesObj){
                for (Role roleUser: roles){
                    if (roleObj==roleUser){
                        response.add(obj);
                    }
                }
            }
        }
        return response;
    }

    public CustomTable newProjectTable(CustomTablePayload customTablePayload){
        Optional<CustomTable> projectTable = customTableRepository.findByName(customTablePayload.getName());
        if (projectTable.isPresent()){
            return null;
        }
        CustomTable newprojectTable = new CustomTable();
        newprojectTable.setName(customTablePayload.getName());
        return customTableRepository.save(newprojectTable);
    }
    public CustomTable getCustomTableById(String id){
        Optional<CustomTable> customTable = customTableRepository.findById(id);
        return customTable.orElse(null);
    }

    public CustomTable save(CustomTable customTable){
        return customTableRepository.save(customTable);
    }


    public Boolean isPresent(String id){
        if (id==null)
            return false;
        return customTableRepository.findById(id).isPresent();
    }

}

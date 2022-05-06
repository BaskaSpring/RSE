package com.baska.RSE.DAO;

import com.baska.RSE.Models.ProjectTable;
import com.baska.RSE.Models.Role;
import com.baska.RSE.Payload.Constructor.ProjectTablePayload;
import com.baska.RSE.Repositories.ProjectTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProjectTableDAO {

    @Autowired
    ProjectTableRepository projectTableRepository;

    public Set<ProjectTable> getProjectsByRoles(Set<Role> roles){
        List<ProjectTable> projectTables = projectTableRepository.getEnabledProjects(true);
        Set<ProjectTable> response = new HashSet<>();
        for (ProjectTable obj: projectTables){
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


    public ProjectTable newProjectTable(ProjectTablePayload projectTablePayload){
        Optional<ProjectTable> projectTable = projectTableRepository.findByName(projectTablePayload.getName());
        if (projectTable.isPresent()){
            return null;
        }
        ProjectTable newprojectTable = new ProjectTable();
        newprojectTable.setName(projectTablePayload.getName());
        return projectTableRepository.save(newprojectTable);
    }
    public ProjectTable getProjectTableById(String id){
        Optional<ProjectTable> projectTable = projectTableRepository.findById(id);
        return projectTable.orElse(null);
    }

    public ProjectTable save(ProjectTable projectTable){
        return projectTableRepository.save(projectTable);
    }


    public Boolean isPresent(String id){
        if (id==null)
            return false;
        return projectTableRepository.findById(id).isPresent();
    }


}

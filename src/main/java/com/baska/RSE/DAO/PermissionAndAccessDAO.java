package com.baska.RSE.DAO;

import com.baska.RSE.Models.ObjectData;
import com.baska.RSE.Models.CustomTable;
import com.baska.RSE.Models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Set;

@Component
public class PermissionAndAccessDAO {

    @Autowired
    CustomTableDAO customTableDAO;

    @Autowired
    ObjectsDAO objectsDAO;

    @Autowired
    UserDAO userDAO;


    public boolean checkObjectsUser(String objectsId,String userName){
        ObjectData objectData = objectsDAO.getObject(objectsId);
        if (objectData ==null){
            return false;
        }
        CustomTable customTable = customTableDAO.getCustomTableById(objectData.getProjectTableId());
        if (customTable ==null){
            return false;
        }
        Set<Role> rolesProject = customTable.getRoles();
        Set<Role> rolesUser = userDAO.getRoles(userName);
        for (Role elproj :rolesProject){
            for (Role eluser: rolesUser){
                if (elproj == eluser) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean checkProjectTableUser(String projectTableId,String userName){
        CustomTable customTable = customTableDAO.getCustomTableById(projectTableId);
        if (customTable ==null){
            return false;
        }
        Set<Role> rolesProject = customTable.getRoles();
        Set<Role> rolesUser = userDAO.getRoles(userName);
        for (Role elproj :rolesProject){
            for (Role eluser: rolesUser){
                if (elproj == eluser) {
                    return true;
                }
            }
        }
        return false;
    }
}

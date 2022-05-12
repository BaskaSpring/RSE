package com.baska.RSE.DAO;


import com.baska.RSE.Models.ObjectData;
import com.baska.RSE.Models.ProjectTable;
import com.baska.RSE.Models.User;
import com.baska.RSE.Repositories.ObjectsDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ObjectsDAO {
    @Autowired
    ObjectsDataRepository objectsDataRepository;


    public ObjectData newObject(ProjectTable projectTable){
        ObjectData objectData = new ObjectData();
        objectData.setEnabled(true);
        objectData.setProjectTableId(projectTable.getId());
        return objectsDataRepository.save(objectData);
    }

    public ObjectData getObject(String id){
        return  objectsDataRepository.getObjectsById(id).orElse(null);
    }

    public ObjectData getObjectsDataOrCreateNew(String idObject, User user,String projectTableId) {
         Optional<ObjectData> objectData = objectsDataRepository.getObjectsById(idObject);
         if (objectData.isPresent()){
             return objectData.get();
         } else{
             ObjectData newObjectData = new ObjectData();
             newObjectData.setUserId(user.getId());
             newObjectData.setEnabled(true);
             newObjectData.setProjectTableId(projectTableId);
             return objectsDataRepository.save(newObjectData);
         }

    }
}

package com.baska.RSE.DAO;


import com.baska.RSE.Models.*;
import com.baska.RSE.Payload.Tables.MapPayload;
import com.baska.RSE.Repositories.ObjectsDataRepository;
import com.baska.RSE.Repositories.TableValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ObjectsDAO {
    @Autowired
    ObjectsDataRepository objectsDataRepository;

    @Autowired
    TableValuesRepository tableValuesRepository;

    public ObjectData newObject(CustomTable customTable){
        ObjectData objectData = new ObjectData();
        objectData.setEnabled(true);
        objectData.setProjectTableId(customTable.getId());
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

    public ObjectData addNewRow(MapPayload object, ObjectData objectsData) {
        TableValues tableValues = new TableValues();
        tableValues.setRow(objectsData.getValues().size()+1);
        Map<Long,String> newRowValues = new HashMap<>();
        for (Types column : object.getObjects().keySet()){
            String s =object.getObjects().get(column);
            if (s==null) {
                s = "false";
            }
            newRowValues.put(column.getId(),s);
        }
        tableValues.setRowValues(newRowValues);
        TableValues newRow = tableValuesRepository.save(tableValues);
        List<TableValues> tableValuesList = objectsData.getValues();
        tableValuesList.add(newRow);
        objectsData.setValues(tableValuesList);
        return objectsDataRepository.save(objectsData);
    }
}

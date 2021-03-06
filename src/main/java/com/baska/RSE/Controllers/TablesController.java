package com.baska.RSE.Controllers;


import com.baska.RSE.DAO.ObjectsDAO;
import com.baska.RSE.DAO.PermissionAndAccessDAO;
import com.baska.RSE.DAO.CustomTableDAO;
import com.baska.RSE.DAO.UserDAO;
import com.baska.RSE.Models.*;
import com.baska.RSE.Models.ObjectData;
import com.baska.RSE.Payload.Tables.MapPayload;
import com.baska.RSE.Payload.Tables.PropPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/tables")
public class TablesController {

    @Autowired
    UserDAO userDAO;

    @Autowired
    CustomTableDAO customTableDAO;

    @Autowired
    ObjectsDAO objectsDAO;

    @Autowired
    PermissionAndAccessDAO permissionAndAccessDAO;

    @PostMapping("/{tableId}/{idObject}")
    public String postNew(@ModelAttribute("object") MapPayload object,
                          @PathVariable String tableId,
                          @PathVariable String idObject,
                          Principal principal,
                          Model model) {
        boolean havePermission = permissionAndAccessDAO.checkObjectsUser(idObject,principal.getName());
        if (!havePermission){
            return "redirect:/tables/index";
        }
        ObjectData objectsData = objectsDAO.getObject(idObject);
        if (objectsData==null){
            return "redirect:/tables/index";
        }
        objectsDAO.addNewRow(object,objectsData);
        return "redirect:/tables/"+tableId+"/"+idObject;
    }

    @GetMapping("/{tableId}/{idObject}")
    public String getEditObject(@PathVariable String tableId,@PathVariable String idObject, Principal principal,Model model) {
        boolean havePermission = permissionAndAccessDAO.checkObjectsUser(idObject,principal.getName());
        if (!havePermission){
            return "redirect:/tables/index";
        }
        User user = userDAO.getUserByUserName(principal.getName());
        ObjectData objectsData = objectsDAO.getObjectsDataOrCreateNew(idObject,user,tableId);
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        MapPayload mapPayload = new MapPayload();
        Map<Types,String> columnStringMap = new HashMap<>();
        HashMap<Long,List<String>> enumValues = new HashMap<>();
        Set<Types> tableColumns = customTable.getColumns();
        for (Types el: tableColumns) {
            columnStringMap.put(el, "");
            if (el.getEType()== EType.ENUM){
                Long key = el.getId();
                List<String> values = new ArrayList<>();
                values.addAll(el.getEnumValue().getEnumTypes());
                enumValues.put(key,values);
            }
        }
        List<Map<Long,String>> tableValues = new ArrayList<>();
        for (TableValues value:objectsData.getValues()){
            Map<Long,String> rowValue = value.getRowValues();
            tableValues.add(rowValue);
        }
        PropPayload propPayload = new PropPayload();
        propPayload.setValues(objectsData.getPropValues().getPropValues());
        Set<Types> tableProps = customTable.getProps();

        mapPayload.setObjects(columnStringMap);
        model.addAttribute("tableProps",tableProps);
        model.addAttribute("propPayload",propPayload);
        model.addAttribute("tableValues",tableValues);
        model.addAttribute("objectsData",objectsData);
        model.addAttribute("mapPayload",mapPayload);
        model.addAttribute("columns",tableColumns);
        model.addAttribute("customTable", customTable);
        return "tables/editTable";
    }


    @GetMapping("/{projectTableId}/new")
    public String getNew(@PathVariable String projectTableId, Principal principal,Model model) {
        boolean havePermission = permissionAndAccessDAO.checkProjectTableUser(projectTableId,principal.getName());
        if (!havePermission){
            return "redirect:/tables/index";
        }
        CustomTable customTable = customTableDAO.getCustomTableById(projectTableId);
        ObjectData objectData = objectsDAO.newObject(customTable);
        return "redirect:/tables/"+ customTable.getId()+"/"+ objectData.getId();
    }

    @GetMapping("/index")
    public String getTables(Model model, Principal principal) {
        Set<Role> roles = userDAO.getRoles(principal.getName());
        Set<CustomTable> customTables = customTableDAO.getProjectsByRoles(roles);
        model.addAttribute("tables", customTables);
        return "tables/index";
    }

    @GetMapping("/{projectTableId}")
    public String getTables(@PathVariable String projectTableId, Principal principal,Model model) {
        boolean havePermission = permissionAndAccessDAO.checkProjectTableUser(projectTableId,principal.getName());
        if (!havePermission){
            return "redirect:/tables/index";
        }
        CustomTable customTable = customTableDAO.getCustomTableById(projectTableId);
        model.addAttribute("object", customTable);
        return "tables/tables";
    }




}

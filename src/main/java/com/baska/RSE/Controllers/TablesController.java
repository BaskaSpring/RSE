package com.baska.RSE.Controllers;


import com.baska.RSE.DAO.ObjectsDAO;
import com.baska.RSE.DAO.PermissionAndAccessDAO;
import com.baska.RSE.DAO.ProjectTableDAO;
import com.baska.RSE.DAO.UserDAO;
import com.baska.RSE.Models.*;
import com.baska.RSE.Models.ObjectData;
import com.baska.RSE.Payload.Tables.MapPayload;
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
    ProjectTableDAO projectTableDAO;

    @Autowired
    ObjectsDAO objectsDAO;

    @Autowired
    PermissionAndAccessDAO permissionAndAccessDAO;

    @GetMapping("/{projectTableId}/{idObject}")
    public String getEditObject(@PathVariable String projectTableId,@PathVariable String idObject, Principal principal,Model model) {
        boolean havePermission = permissionAndAccessDAO.checkObjectsUser(idObject,principal.getName());
        if (!havePermission){
            return "redirect:/tables/index";
        }
        User user = userDAO.getUserByUserName(principal.getName());
        ObjectData objectsData = objectsDAO.getObjectsDataOrCreateNew(idObject,user,projectTableId);
        ProjectTable projectTable = projectTableDAO.getProjectTableById(projectTableId);
        MapPayload mapPayload = new MapPayload();
        Map<TableColumn,String> columnStringMap = new HashMap<>();
        HashMap<Long,List<String>> enumValues = new HashMap<>();
        Set<TableColumn> tableColumns = projectTable.getColumns();
        tableColumns.forEach(x->System.out.println(x.getName()));
        for (TableColumn el: tableColumns) {
            columnStringMap.put(el, "");
            if (el.getType()== Type.ENUM){
                Long key = el.getId();
                List<String> values = new ArrayList<>();
                el.getEnumTypes().forEach(x->values.add(x.getValue()));
                enumValues.put(key,values);
            }
        }
        mapPayload.setObjects(columnStringMap);
        model.addAttribute("objectsData",objectsData);
        model.addAttribute("mapPayload",mapPayload);
        model.addAttribute("columns",tableColumns);
        model.addAttribute("projectTable",projectTable);
        return "tables/editTable";
    }


    @PostMapping("/{projectTableId}/{idObject}")
    public String postNew(@ModelAttribute("object") MapPayload object,
                          @PathVariable String projectTableId,
                          @PathVariable String idObject,
                          Principal principal,
                          Model model) {
        boolean havePermission = permissionAndAccessDAO.checkObjectsUser(idObject,principal.getName());
        if (!havePermission){
            return "redirect:/tables/index";
        }
        User user = userDAO.getUserByUserName(principal.getName());
        ObjectData objectsData = objectsDAO.getObject(idObject);
        if (objectsData==null){
            return "redirect:/tables/index";
        }
        return "redirect:/tables/"+projectTableId+"/"+idObject;
    }


    @GetMapping("/{projectTableId}/new")
    public String getNew(@PathVariable String projectTableId, Principal principal,Model model) {
        boolean havePermission = permissionAndAccessDAO.checkProjectTableUser(projectTableId,principal.getName());
        if (!havePermission){
            return "redirect:/tables/index";
        }
        ProjectTable projectTable = projectTableDAO.getProjectTableById(projectTableId);
        ObjectData objectData = objectsDAO.newObject(projectTable);
        return "redirect:/tables/"+projectTable.getId()+"/"+ objectData.getId();
    }

    @GetMapping("/index")
    public String getTables(Model model, Principal principal) {
        Set<Role> roles = userDAO.getRoles(principal.getName());
        Set<ProjectTable> projectTables = projectTableDAO.getProjectsByRoles(roles);
        model.addAttribute("tables",projectTables);
        return "tables/index";
    }

    @GetMapping("/{projectTableId}")
    public String getTables(@PathVariable String projectTableId, Principal principal,Model model) {
        boolean havePermission = permissionAndAccessDAO.checkProjectTableUser(projectTableId,principal.getName());
        if (!havePermission){
            return "redirect:/tables/index";
        }
        ProjectTable projectTable = projectTableDAO.getProjectTableById(projectTableId);
        model.addAttribute("object",projectTable);
        return "tables/tables";
    }




}

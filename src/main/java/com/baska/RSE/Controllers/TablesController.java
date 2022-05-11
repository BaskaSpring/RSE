package com.baska.RSE.Controllers;


import com.baska.RSE.DAO.ProjectTableDAO;
import com.baska.RSE.DAO.UserDAO;
import com.baska.RSE.Models.ProjectTable;
import com.baska.RSE.Models.Role;
import com.baska.RSE.Models.TableColumn;
import com.baska.RSE.Models.Type;
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


    @PostMapping("/{id}/new")
    public String postNew(@ModelAttribute("object") MapPayload object, @PathVariable String id, Principal principal, Model model) {
        if (!projectTableDAO.isPresent(id)){
            return "redirect:/tables/index";
        }
        ProjectTable projectTable = projectTableDAO.getProjectTableById(id);
        Set<Role> rolesProject = projectTable.getRoles();
        Set<Role> rolesUser = userDAO.getRoles(principal.getName());
        boolean havePermission = false;
        for (Role elproj :rolesProject){
            for (Role eluser: rolesUser){
                if (elproj == eluser) {
                    havePermission = true;
                    break;
                }
            }
            if (havePermission){
                break;
            }
        }
        if (!havePermission){
            return "redirect:/tables/index";
        }
        HashMap<Long,List<String>> enumValues = new HashMap<>();
        Set<TableColumn> tableColumns = projectTable.getColumns();
        tableColumns.forEach(x->System.out.println(x.getName()));
        for (TableColumn el: tableColumns) {
            if (el.getType()== Type.ENUM){
                Long key = el.getId();
                List<String> values = new ArrayList<>();
                el.getEnumTypes().forEach(x->values.add(x.getValue()));
                enumValues.put(key,values);
            }
        }
        MapPayload mapPayload = new MapPayload();
        model.addAttribute("object",mapPayload.getObject());
        model.addAttribute("enumValues",enumValues);
        model.addAttribute("columns",tableColumns);
        model.addAttribute("projectTable",projectTable);
        return "tables/new";
    }

    @GetMapping("/{id}/new")
    public String getNew(@PathVariable String id, Principal principal,Model model) {
        if (!projectTableDAO.isPresent(id)){
            return "redirect:/tables/index";
        }
        ProjectTable projectTable = projectTableDAO.getProjectTableById(id);
        Set<Role> rolesProject = projectTable.getRoles();
        Set<Role> rolesUser = userDAO.getRoles(principal.getName());
        boolean havePermission = false;
        for (Role elproj :rolesProject){
            for (Role eluser: rolesUser){
                if (elproj == eluser) {
                    havePermission = true;
                    break;
                }
            }
            if (havePermission){
                break;
            }
        }
        if (!havePermission){
            return "redirect:/tables/index";
        }
        MapPayload mapPayload = new MapPayload();
        Map<TableColumn,String> columnStringMap = new HashMap<>();

        HashMap<Long,List<String>> enumValues = new HashMap<>();
        Set<TableColumn> tableColumns = projectTable.getColumns();
        tableColumns.forEach(x->System.out.println(x.getName()));
        for (TableColumn el: tableColumns) {
            if (el.getType()== Type.ENUM){
                Long key = el.getId();
                List<String> values = new ArrayList<>();
                el.getEnumTypes().forEach(x->values.add(x.getValue()));
                enumValues.put(key,values);
            }
            columnStringMap.put(el, "");
        }
        mapPayload.setObject(columnStringMap);
        model.addAttribute("mapPayload",mapPayload);
        model.addAttribute("enumValues",enumValues);
        model.addAttribute("columns",tableColumns);
        model.addAttribute("projectTable",projectTable);
        return "tables/new";
    }


    @GetMapping("/index")
    public String getTables(Model model, Principal principal) {
        Set<Role> roles = userDAO.getRoles(principal.getName());
        Set<ProjectTable> projectTables = projectTableDAO.getProjectsByRoles(roles);

        model.addAttribute("tables",projectTables);
        return "tables/index";
    }

    @GetMapping("/{id}")
    public String getTables(@PathVariable String id, Principal principal,Model model) {
        if (!projectTableDAO.isPresent(id)){
            return "redirect:/tables/index";
        }
        ProjectTable projectTable = projectTableDAO.getProjectTableById(id);
        Set<Role> rolesProject = projectTable.getRoles();
        Set<Role> rolesUser = userDAO.getRoles(principal.getName());
        boolean havePermission = false;
        for (Role elproj :rolesProject){
            for (Role eluser: rolesUser){
                if (elproj==eluser){
                    havePermission=true;
                }
            }
        }
        if (!havePermission){
            return "redirect:/tables/index";
        }
        model.addAttribute("object",projectTable);
        return "tables/table";
    }




}

package com.baska.RSE.Controllers;


import com.baska.RSE.DAO.ProjectTableDAO;
import com.baska.RSE.DAO.UserDAO;
import com.baska.RSE.Models.ProjectTable;
import com.baska.RSE.Models.Role;
import com.baska.RSE.Models.TableColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/tables")
public class TablesController {

    @Autowired
    UserDAO userDAO;

    @Autowired
    ProjectTableDAO projectTableDAO;

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
        Set<TableColumn> tableColumns = projectTable.getColumns();
        tableColumns.forEach(x->System.out.println(x.getName()));
        model.addAttribute("columns",tableColumns);
        model.addAttribute("object",projectTable);
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

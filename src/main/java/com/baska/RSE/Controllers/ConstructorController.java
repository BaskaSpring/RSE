package com.baska.RSE.Controllers;

import com.baska.RSE.DAO.ProjectTableDAO;
import com.baska.RSE.DAO.RoleDAO;
import com.baska.RSE.DAO.TableColumnDAO;
import com.baska.RSE.Models.ProjectTable;
import com.baska.RSE.Models.Role;
import com.baska.RSE.Models.TableColumn;
import com.baska.RSE.Models.Type;
import com.baska.RSE.Payload.Constructor.ColumnsPayload;
import com.baska.RSE.Payload.Constructor.ProjectTablePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/constructor")
public class ConstructorController {

    @Autowired
    RoleDAO roleDAO;

    @Autowired
    ProjectTableDAO projectTableDAO;

    @Autowired
    TableColumnDAO tableColumnDAO;


    @GetMapping("/columns/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getColumns(@PathVariable String id,Model model) {
        if (!projectTableDAO.isPresent(id)){
            return "redirect:/constructor/index";
        }
        ProjectTable projectTable = projectTableDAO.getProjectTableById(id);
        List<Type> types = new ArrayList<>();
        types.add(Type.BOOLEAN);
        types.add(Type.ENUM);
        types.add(Type.NUMBER);
        types.add(Type.STRING);
        model.addAttribute("types",types);
        model.addAttribute("columnsPayload",new ColumnsPayload());
        model.addAttribute("projectTable",projectTable);
        return "constructor/columns";
    }

    @GetMapping("/columns/{id}/save")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getSave(@PathVariable String id,Model model) {
        if (!projectTableDAO.isPresent(id)){
            return "redirect:/constructor/index";
        }
        ProjectTable projectTable = projectTableDAO.getProjectTableById(id);
        projectTable.setEnabled(true);
        projectTableDAO.save(projectTable);
        return "redirect:/constructor/index";
    }

    @PostMapping("/columns/{id}/addColumn")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postAddColumn(@PathVariable String id,
                                @ModelAttribute("columnsPayload") @Valid ColumnsPayload columnsPayload,
                                BindingResult bindingResult,
                                Model model) {
        ProjectTable projectTable = projectTableDAO.getProjectTableById(id);
        if (projectTable!=null) {
            if (bindingResult.hasErrors())
                return "constructor/columns"+projectTable.getId();
            List<Type> types = new ArrayList<>();
            types.add(Type.BOOLEAN);
            types.add(Type.ENUM);
            types.add(Type.NUMBER);
            types.add(Type.STRING);
            TableColumn tableColumn = new TableColumn();
            tableColumn.setName(columnsPayload.getName());
            boolean finded = false;
            if (Type.BOOLEAN.toString().equals(columnsPayload.getType())) {
                finded = true;
                tableColumn.setType(Type.BOOLEAN);
            }
            if (Type.ENUM.toString().equals(columnsPayload.getType())) {
                finded = true;
                tableColumn.setType(Type.ENUM);
            }
            if (Type.STRING.toString().equals(columnsPayload.getType())) {
                finded = true;
                tableColumn.setType(Type.STRING);
            }
            if (Type.NUMBER.toString().equals(columnsPayload.getType())) {
                finded = true;
                tableColumn.setType(Type.NUMBER);
            }
            if (finded){
                TableColumn newTableColumn = tableColumnDAO.save(tableColumn);
                projectTable.getColumns().add(newTableColumn);
                ProjectTable resp = projectTableDAO.save(projectTable);
                model.addAttribute("types",types);
                model.addAttribute("projectTable",resp);

                return "redirect:/constructor/columns/" + resp.getId();
            }
            return "redirect:/constructor/index";
        }
        return "redirect:/constructor/index";
    }


    @GetMapping("/index")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getConstructor(Model model) {
        return "constructor/index";
    }

    @GetMapping("/createProjectTable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getCreate(Model model) {
        ProjectTablePayload projectTablePayload = new ProjectTablePayload();
        model.addAttribute("projectTablePayload", projectTablePayload);
        return "constructor/createProjectTable";
    }

    @PostMapping("/createProjectTable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postCreate(@ModelAttribute("projectTablePayload") @Valid ProjectTablePayload projectTablePayload,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "constructor/createProjectTable";

        ProjectTable projectTable = projectTableDAO.newProjectTable(projectTablePayload);
        if (projectTable==null) {
            FieldError error = new FieldError("ProjectTablePayload","name","Имя не уникально");
            bindingResult.addError(error);
            return "constructor/createProjectTable";
        }
        return "redirect:/constructor/selectRoles/"+projectTable.getId();
    }

    @GetMapping("/selectRoles/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getSelectRoles(@PathVariable String id,Model model) {
        if (!projectTableDAO.isPresent(id)){
            return "redirect:/constructor/index";
        }
        ProjectTable projectTable = projectTableDAO.getProjectTableById(id);
        List<Role> roles = roleDAO.getRoles();
        model.addAttribute("roles",roles);
        model.addAttribute("projectTable",projectTable);
        return "constructor/selectRoles";
    }


    @PostMapping("/selectRoles/{id}/addrole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postSelectRoles(@PathVariable String id,@RequestParam Long roleId,Model model) {
        Role role = roleDAO.getRoleById(roleId);
        ProjectTable projectTable = projectTableDAO.getProjectTableById(id);
        if (projectTable!=null){
            if (role!=null){
                projectTable.getRoles().add(role);
                ProjectTable resp = projectTableDAO.save(projectTable);
                model.addAttribute("projectTable",resp);
                model.addAttribute("roles",roleDAO.getRoles());
                return "redirect:/constructor/selectRoles/" + resp.getId();
            }
            return "redirect:/constructor/index";
        }
        return "redirect:/constructor/index";
    }

}

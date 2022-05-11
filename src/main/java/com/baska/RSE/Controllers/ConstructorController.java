package com.baska.RSE.Controllers;

import com.baska.RSE.DAO.EnumValuesDAO;
import com.baska.RSE.DAO.ProjectTableDAO;
import com.baska.RSE.DAO.RoleDAO;
import com.baska.RSE.DAO.TableColumnDAO;
import com.baska.RSE.Models.*;
import com.baska.RSE.Payload.Constructor.ColumnsPayload;
import com.baska.RSE.Payload.Constructor.EnumPayload;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/constructor")
public class ConstructorController {

    @Autowired
    RoleDAO roleDAO;

    @Autowired
    ProjectTableDAO projectTableDAO;

    @Autowired
    TableColumnDAO tableColumnDAO;

    @Autowired
    EnumValuesDAO enumValuesDAO;

    @PostMapping("/columns/{id}/editEnum/{columnId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postEditEnum(@PathVariable("id") String id,
                               @PathVariable("columnId") Long columnId,
                               @ModelAttribute("enumPayload") @Valid EnumPayload enumPayload,
                               BindingResult bindingResult,
                               Model model) {
        ProjectTable projectTable = projectTableDAO.getProjectTableById(id);
        if (projectTable != null) {
            TableColumn tableColumn = tableColumnDAO.getById(columnId);
            if (bindingResult.hasErrors())
            {
//                "dasd";
            }
            if (tableColumn != null) {
                EnumValues enumValues = new EnumValues();
                enumValues.setValue(enumPayload.getName());
                EnumValues resp = enumValuesDAO.save(enumValues);
                Set<EnumValues> enumValuesSet = tableColumn.getEnumTypes();
                enumValuesSet.add(resp);
                tableColumn.setEnumTypes(enumValuesSet);
                TableColumn response = tableColumnDAO.save(tableColumn);
                model.addAttribute("enumPayload", new EnumPayload());
                model.addAttribute("projectTable", projectTable);
                model.addAttribute("column", response);
                return "constructor/editEnum";
            }
            return "redirect:/constructor/index";
        }
        return "redirect:/constructor/index";
    }


    @GetMapping("/columns/{id}/editEnum/{columnId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditEnum(@PathVariable("id") String id,
                            @PathVariable("columnId") Long columnId,
                            Model model) {
        ProjectTable projectTable = projectTableDAO.getProjectTableById(id);
        if (projectTable!=null) {
            TableColumn tableColumn = tableColumnDAO.getById(columnId);
            if (tableColumn != null) {
                model.addAttribute("enumPayload",new EnumPayload());
                model.addAttribute("projectTable",projectTable);
                model.addAttribute("column",tableColumn);
                return "constructor/editEnum";
            }
            return "redirect:/constructor/index";
        }
        return "redirect:/constructor/index";
    }


    @GetMapping("/columns/{id}/edit/{columnId}/up")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditUp(@PathVariable("id") String id,
                                @PathVariable("columnId") Long columnId,
                                Model model) {
        ProjectTable projectTable = projectTableDAO.getProjectTableById(id);
        if (projectTable!=null) {
            boolean finded = false;
            TableColumn tableColumn = tableColumnDAO.getById(columnId);
            if (tableColumn != null) {
                if (tableColumn.getPriority() > 1) {
                    finded = true;
                    Set<TableColumn> columnSet = projectTable.getColumns();
                    TableColumn swap = new TableColumn();
                    for (TableColumn el : columnSet) {
                        if (el.getPriority() + 1 == tableColumn.getPriority()) {
                            swap = el;
                            break;
                        }
                    }
                    int prior = swap.getPriority();
                    swap.setPriority(tableColumn.getPriority());
                    tableColumn.setPriority(prior);
                    tableColumnDAO.save(swap);
                    tableColumnDAO.save(tableColumn);
                } else{
                    return "redirect:/constructor/columns/" + projectTable.getId();
                }
            }
            if (finded) {
                ProjectTable resp = projectTableDAO.save(projectTable);
                return "redirect:/constructor/columns/" + resp.getId();
            }
            return "redirect:/constructor/index";
        }
        return "redirect:/constructor/index";
    }

    @GetMapping("/columns/{id}/edit/{columnId}/down")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditDown(@PathVariable("id") String id,
                              @PathVariable("columnId") Long columnId,
                              Model model){
        ProjectTable projectTable = projectTableDAO.getProjectTableById(id);
        if (projectTable!=null) {
            boolean finded = false;
            TableColumn tableColumn = tableColumnDAO.getById(columnId);
            if (tableColumn != null) {
                if (tableColumn.getPriority() < projectTable.getColumns().size()) {
                    finded = true;
                    Set<TableColumn> columnSet = projectTable.getColumns();
                    TableColumn swap = new TableColumn();
                    for (TableColumn el : columnSet) {
                        if (el.getPriority() == tableColumn.getPriority()+1) {
                            swap = el;
                            break;
                        }
                    }
                    int prior = swap.getPriority();
                    swap.setPriority(tableColumn.getPriority());
                    tableColumn.setPriority(prior);
                    tableColumnDAO.save(swap);
                    tableColumnDAO.save(tableColumn);
                } else {
                    return "redirect:/constructor/columns/" + projectTable.getId();
                }
            }
            if (finded) {
                ProjectTable resp = projectTableDAO.save(projectTable);
                return "redirect:/constructor/columns/" + resp.getId();
            }
            return "redirect:/constructor/index";
        }
        return "redirect:/constructor/index";
    }



    @GetMapping("/columns/{id}/edit/{columnId}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditDelete(@PathVariable("id") String id,
                              @PathVariable("columnId") Long columnId,
                              Model model){
        ProjectTable projectTable = projectTableDAO.getProjectTableById(id);
        if (projectTable!=null) {
            boolean finded = false;
            TableColumn tableColumn = tableColumnDAO.getById(columnId);
            if (tableColumn != null) {
                finded = true;
                int priority = tableColumn.getPriority();
                Set<TableColumn> tableColumnSet = projectTable.getColumns();
                tableColumnSet.remove(tableColumn);
                Set<TableColumn> newTable = new HashSet<>();
                for (TableColumn el : tableColumnSet) {
                    if (el.getPriority()>priority){
                        el.setPriority(el.getPriority()-1);
                    }
                    newTable.add(tableColumnDAO.save(el));
                }
                projectTable.setColumns(newTable);
            }
            if (finded) {
                ProjectTable resp = projectTableDAO.save(projectTable);
                tableColumnDAO.delete(tableColumn);
                return "redirect:/constructor/columns/" + resp.getId();
            }
            return "redirect:/constructor/index";
        }
        return "redirect:/constructor/index";
    }


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
        types.add(Type.DATE);
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
            TableColumn tableColumn = new TableColumn();
            tableColumn.setName(columnsPayload.getName());
            boolean finded = false;
            if (Type.BOOLEAN.toString().equals(columnsPayload.getType())) {
                finded = true;
                tableColumn.setType(Type.BOOLEAN);
            }
            if (Type.DATE.toString().equals(columnsPayload.getType())) {
                finded = true;
                tableColumn.setType(Type.DATE);
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
                int priority = 1;
                if (!projectTable.getColumns().isEmpty()){
                    priority = projectTable.getColumns().size()+1;
                }
                tableColumn.setPriority(priority);
                TableColumn newTableColumn = tableColumnDAO.save(tableColumn);
                Set<TableColumn> tableColumnSet = projectTable.getColumns();
                tableColumnSet.add(newTableColumn);
                projectTable.setColumns(tableColumnSet);
                ProjectTable resp = projectTableDAO.save(projectTable);
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

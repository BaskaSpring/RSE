package com.baska.RSE.Controllers;

import com.baska.RSE.DAO.EnumValuesDAO;
import com.baska.RSE.DAO.CustomTableDAO;
import com.baska.RSE.DAO.RoleDAO;
import com.baska.RSE.DAO.TableColumnDAO;
import com.baska.RSE.Models.*;
import com.baska.RSE.Payload.Constructor.TypePayload;
import com.baska.RSE.Payload.Constructor.CustomTablePayload;
import com.baska.RSE.Payload.Constructor.EnumPayload;
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
    CustomTableDAO customTableDAO;

    @Autowired
    TableColumnDAO tableColumnDAO;

    @Autowired
    EnumValuesDAO enumValuesDAO;


    @GetMapping("/edit/{tableId}/editProps/{propId}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditPropDelete(@PathVariable("tableId") String tableId,
                                    @PathVariable("propId") Long propId) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types type = tableColumnDAO.getById(propId);
        if (type == null) {
            return "redirect:/constructor/index";
        }
        int priority = type.getPriority();
        Set<Types> typesSet = customTable.getProps();
        typesSet.remove(type);
        Set<Types> newTable = new HashSet<>();
        for (Types el : typesSet) {
            if (el.getPriority() > priority) {
                el.setPriority(el.getPriority() - 1);
            }
            newTable.add(tableColumnDAO.save(el));
        }
        customTable.setProps(newTable);
        CustomTable resp = customTableDAO.save(customTable);
        tableColumnDAO.delete(type);
        return "redirect:/constructor/edit/"+resp.getId()+"/editProps";
    }

    @PostMapping("/edit/{tableId}/editProps/addProp")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postAddProp(@PathVariable String tableId,
                              @ModelAttribute("typePayload") @Valid TypePayload typePayload,
                              BindingResult bindingResult,
                              Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable ==null) {
            return "redirect:/constructor/index";
        }
        if (bindingResult.hasErrors()) {
            List<EType> ETypes = new ArrayList<>();
            ETypes.add(EType.BOOLEAN);
            ETypes.add(EType.ENUM);
            ETypes.add(EType.NUMBER);
            ETypes.add(EType.STRING);
            ETypes.add(EType.DATE);
            model.addAttribute("ETypes", ETypes);
            model.addAttribute("table", customTable);
            return "constructor/editProps";
        }

        Types types = new Types();
        types.setName(typePayload.getName());
        boolean finded = false;
        if (EType.BOOLEAN.name().equals(typePayload.getType())) {
            finded = true;
            types.setEType(EType.BOOLEAN);
        }
        if (EType.DATE.name().equals(typePayload.getType())) {
            finded = true;
            types.setEType(EType.DATE);
        }
        if (EType.ENUM.name().equals(typePayload.getType())) {
            finded = true;
            types.setEType(EType.ENUM);
        }
        if (EType.STRING.name().equals(typePayload.getType())) {
            finded = true;
            types.setEType(EType.STRING);
        }
        if (EType.NUMBER.name().equals(typePayload.getType())) {
            finded = true;
            types.setEType(EType.NUMBER);
        }
        if (!finded) {
            return "redirect:/constructor/index";
        }
        int priority = 1;
        if (!customTable.getColumns().isEmpty()) {
            priority = customTable.getProps().size() + 1;
        }
        types.setPriority(priority);
        Types newTableValue = tableColumnDAO.save(types);
        Set<Types> tableValueSet = customTable.getProps();
        tableValueSet.add(newTableValue);
        customTable.setProps(tableValueSet);
        CustomTable resp = customTableDAO.save(customTable);
        return "redirect:/constructor/edit/"+resp.getId()+"/editProps";
    }



    @GetMapping("/edit/{tableId}/editProps/{propId}/up")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditPropsUp(@PathVariable("tableId") String tableId,
                                 @PathVariable("propId") Long propId) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types type = tableColumnDAO.getById(propId);
        if (type == null) {
            return "redirect:/constructor/index";
        }
        if (type.getPriority() <= 1) {
            return "redirect:/constructor/edit/"+customTable.getId()+"/editProps/";
        }
        Set<Types> typeSet = customTable.getProps();
        Types swap = new Types();
        for (Types el : typeSet) {
            if (el.getPriority() + 1 == type.getPriority()) {
                swap = el;
                break;
            }
        }
        int prior = swap.getPriority();
        swap.setPriority(type.getPriority());
        type.setPriority(prior);
        tableColumnDAO.save(swap);
        tableColumnDAO.save(type);
        CustomTable resp = customTableDAO.save(customTable);
        return "redirect:/constructor/edit/"+resp.getId()+"/editProps/";
    }

    @GetMapping("/edit/{tableId}/editProps/{propId}/down")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditPropsDown(@PathVariable("tableId") String tableId,
                                   @PathVariable("propId") Long propId) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable ==null) {
            return "redirect:/constructor/index";
        }
        Types type = tableColumnDAO.getById(propId);
        if (type == null) {
            return "redirect:/constructor/index";
        }
        if (type.getPriority() >= customTable.getProps().size()) {
            return "redirect:/constructor/edit/"+customTable.getId()+"/editProps/";
        }
        Set<Types> typeSet = customTable.getProps();
        Types swap = new Types();
        for (Types el : typeSet) {
            if (el.getPriority() == type.getPriority() + 1) {
                swap = el;
                break;
            }
        }
        int prior = swap.getPriority();
        swap.setPriority(type.getPriority());
        type.setPriority(prior);
        tableColumnDAO.save(swap);
        tableColumnDAO.save(type);
        CustomTable resp = customTableDAO.save(customTable);
        return "redirect:/constructor/edit/"+resp.getId()+"/editProps/";
    }


    @PostMapping("/edit/{tableId}/addProps")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postAddProps(@PathVariable String tableId,
                               @ModelAttribute("CustomTablePayload") @Valid CustomTablePayload customTablePayload,
                               @RequestParam Long roleId) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Role role = roleDAO.getRoleById(roleId);
        if (role == null) {
            return "redirect:/constructor/index";
        }
        Set<Role> roles = customTable.getRoles();
        roles.add(role);
        customTable.setRoles(roles);
        customTableDAO.save(customTable);
        return "redirect:/constructor/edit/" + customTable.getId() + "/editRole";
    }


    @GetMapping("/edit/{tableId}/editProps")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postEditProps(@PathVariable String tableId,
                               Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        List<EType> ETypes = new ArrayList<>();
        ETypes.add(EType.BOOLEAN);
        ETypes.add(EType.ENUM);
        ETypes.add(EType.NUMBER);
        ETypes.add(EType.STRING);
        ETypes.add(EType.DATE);
        TypePayload typePayload = new TypePayload();
        model.addAttribute("ETypes", ETypes);
        model.addAttribute("typePayload", typePayload);
        model.addAttribute("table", customTable);
        return "constructor/editProps";
    }


    @PostMapping("/edit/{tableId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postEditName(@PathVariable String tableId,
                               @ModelAttribute("CustomTablePayload") @Valid CustomTablePayload customTablePayload,
                               BindingResult bindingResult,
                               Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable==null){
            return "redirect:/constructor/index";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("table", customTable);
            return "constructor/editTable";
        }
        if (customTableDAO.isPresentByName(customTablePayload.getName())){
            FieldError error = new FieldError("CustomTablePayload","name","Имя не уникально");
            bindingResult.addError(error);
            model.addAttribute("table", customTable);
            return "constructor/editTable";
        }
        customTable.setName(customTablePayload.getName());
        customTableDAO.save(customTable);
        return "redirect:/constructor/edit/"+customTable.getId();
    }

    @PostMapping("/edit/{tableId}/addRole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postAddRole(@PathVariable String tableId,
                              @RequestParam Long roleId) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Role role  = roleDAO.getRoleById(roleId);
        if (role==null){
            return "redirect:/constructor/index";
        }
        Set<Role> roles = customTable.getRoles();
        roles.add(role);
        customTable.setRoles(roles);
        customTableDAO.save(customTable);
        return "redirect:/constructor/edit/"+customTable.getId()+"/editRole";
    }

    @GetMapping("/edit/{tableId}/deleteRole/{roleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postDeleteRole(@PathVariable String tableId,
                                 @PathVariable Long roleId) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Role role  = roleDAO.getRoleById(roleId);
        if (role==null){
            return "redirect:/constructor/index";
        }
        Set<Role> roles = customTable.getRoles();
        roles.remove(role);
        customTable.setRoles(roles);
        customTableDAO.save(customTable);
        return "redirect:/constructor/edit/"+customTable.getId()+"/editRole";
    }

    @GetMapping("/edit/{tableId}/editRole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postEditRole(@PathVariable String tableId,
                               Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        List<Role> allRoles = roleDAO.getRoles();
        List<Role> roleList = new ArrayList<>();
        for (Role allrole : allRoles) {
            boolean finded = false;
            for (Role tableRole : customTable.getRoles()) {
                if (tableRole == allrole) {
                    finded = true;
                    break;
                }
            }
            if (!finded) {
                roleList.add(allrole);
            }
        }
        model.addAttribute("roles", roleList);
        model.addAttribute("table", customTable);
        return "constructor/editRole";
    }

    @GetMapping("/edit/{projectTableId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditTable(@PathVariable("projectTableId") String projectTableId,
                                Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(projectTableId);
        if (customTable ==null) {
            return "redirect:/constructor/index";
        }
        CustomTablePayload customTablePayload = new CustomTablePayload();
        customTablePayload.setName(customTable.getName());
        model.addAttribute("CustomTablePayload",customTablePayload);
        model.addAttribute("table", customTable);
        return "constructor/editTable";
    }


    @GetMapping("/tableList")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getTableList(Model model) {
        List<CustomTable> customTableList = customTableDAO.getProjectList();
        model.addAttribute("tableList", customTableList);
        return "constructor/projectTableList";
    }


//    @PostMapping("/columns/{id}/editEnum/{columnId}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public String postEditEnum(@PathVariable("id") String id,
//                               @PathVariable("columnId") Long columnId,
//                               @ModelAttribute("enumPayload") @Valid EnumPayload enumPayload,
//                               BindingResult bindingResult,
//                               Model model) {
//        CustomTable customTable = customTableDAO.getProjectTableById(id);
//        if (customTable != null) {
//            Types tableValue = tableColumnDAO.getById(columnId);
//            if (bindingResult.hasErrors())
//            {
////                "dasd";
//            }
//            if (tableValue != null) {
//                EnumValues enumValues = new EnumValues();
//                enumValues.setValue(enumPayload.getName());
//                EnumValues resp = enumValuesDAO.save(enumValues);
//                Set<EnumValues> enumValuesSet = tableValue.getEnumTypes();
//                enumValuesSet.add(resp);
//                tableValue.setEnumTypes(enumValuesSet);
//                Types response = tableColumnDAO.save(tableValue);
//                model.addAttribute("enumPayload", new EnumPayload());
//                model.addAttribute("projectTable", customTable);
//                model.addAttribute("column", response);
//                return "constructor/editEnum";
//            }
//            return "redirect:/constructor/index";
//        }
//        return "redirect:/constructor/index";
//    }


    @GetMapping("/columns/{id}/editEnum/{columnId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditEnum(@PathVariable("id") String id,
                              @PathVariable("columnId") Long columnId,
                              Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(id);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types tableValue = tableColumnDAO.getById(columnId);
        if (tableValue == null) {
            return "redirect:/constructor/index";
        }
        model.addAttribute("enumPayload", new EnumPayload());
        model.addAttribute("projectTable", customTable);
        model.addAttribute("column", tableValue);
        return "constructor/editEnum";


    }


    @GetMapping("/columns/{id}/edit/{columnId}/up")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditUp(@PathVariable("id") String id,
                                @PathVariable("columnId") Long columnId,
                                Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(id);
        if (customTable !=null) {
            boolean finded = false;
            Types tableValue = tableColumnDAO.getById(columnId);
            if (tableValue != null) {
                if (tableValue.getPriority() > 1) {
                    finded = true;
                    Set<Types> columnSet = customTable.getColumns();
                    Types swap = new Types();
                    for (Types el : columnSet) {
                        if (el.getPriority() + 1 == tableValue.getPriority()) {
                            swap = el;
                            break;
                        }
                    }
                    int prior = swap.getPriority();
                    swap.setPriority(tableValue.getPriority());
                    tableValue.setPriority(prior);
                    tableColumnDAO.save(swap);
                    tableColumnDAO.save(tableValue);
                } else{
                    return "redirect:/constructor/columns/" + customTable.getId();
                }
            }
            if (finded) {
                CustomTable resp = customTableDAO.save(customTable);
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
        CustomTable customTable = customTableDAO.getCustomTableById(id);
        if (customTable !=null) {
            boolean finded = false;
            Types tableValue = tableColumnDAO.getById(columnId);
            if (tableValue != null) {
                if (tableValue.getPriority() < customTable.getColumns().size()) {
                    finded = true;
                    Set<Types> columnSet = customTable.getColumns();
                    Types swap = new Types();
                    for (Types el : columnSet) {
                        if (el.getPriority() == tableValue.getPriority()+1) {
                            swap = el;
                            break;
                        }
                    }
                    int prior = swap.getPriority();
                    swap.setPriority(tableValue.getPriority());
                    tableValue.setPriority(prior);
                    tableColumnDAO.save(swap);
                    tableColumnDAO.save(tableValue);
                } else {
                    return "redirect:/constructor/columns/" + customTable.getId();
                }
            }
            if (finded) {
                CustomTable resp = customTableDAO.save(customTable);
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
        CustomTable customTable = customTableDAO.getCustomTableById(id);
        if (customTable !=null) {
            boolean finded = false;
            Types tableValue = tableColumnDAO.getById(columnId);
            if (tableValue != null) {
                finded = true;
                int priority = tableValue.getPriority();
                Set<Types> tableValueSet = customTable.getColumns();
                tableValueSet.remove(tableValue);
                Set<Types> newTable = new HashSet<>();
                for (Types el : tableValueSet) {
                    if (el.getPriority()>priority){
                        el.setPriority(el.getPriority()-1);
                    }
                    newTable.add(tableColumnDAO.save(el));
                }
                customTable.setColumns(newTable);
            }
            if (finded) {
                CustomTable resp = customTableDAO.save(customTable);
                tableColumnDAO.delete(tableValue);
                return "redirect:/constructor/columns/" + resp.getId();
            }
            return "redirect:/constructor/index";
        }
        return "redirect:/constructor/index";
    }


    @GetMapping("/columns/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getColumns(@PathVariable String id,Model model) {
        if (!customTableDAO.isPresent(id)){
            return "redirect:/constructor/index";
        }
        CustomTable customTable = customTableDAO.getCustomTableById(id);
        List<EType> ETypes = new ArrayList<>();
        ETypes.add(EType.BOOLEAN);
        ETypes.add(EType.ENUM);
        ETypes.add(EType.NUMBER);
        ETypes.add(EType.STRING);
        ETypes.add(EType.DATE);
        model.addAttribute("types", ETypes);
        model.addAttribute("columnsPayload",new TypePayload());
        model.addAttribute("projectTable", customTable);
        return "constructor/columns";
    }

    @GetMapping("/columns/{id}/save")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getSave(@PathVariable String id,Model model) {
        if (!customTableDAO.isPresent(id)){
            return "redirect:/constructor/index";
        }
        CustomTable customTable = customTableDAO.getCustomTableById(id);
        customTable.setEnabled(true);
        customTableDAO.save(customTable);
        return "redirect:/constructor/index";
    }

    @PostMapping("/columns/{id}/addColumn")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postAddColumn(@PathVariable String id,
                                @ModelAttribute("columnsPayload") @Valid TypePayload typePayload,
                                BindingResult bindingResult,
                                Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(id);
        if (customTable !=null) {
            if (bindingResult.hasErrors())
                return "constructor/columns"+ customTable.getId();
            Types tableValue = new Types();
            tableValue.setName(typePayload.getName());
            boolean finded = false;
            if (EType.BOOLEAN.toString().equals(typePayload.getType())) {
                finded = true;
                tableValue.setEType(EType.BOOLEAN);
            }
            if (EType.DATE.toString().equals(typePayload.getType())) {
                finded = true;
                tableValue.setEType(EType.DATE);
            }
            if (EType.ENUM.toString().equals(typePayload.getType())) {
                finded = true;
                tableValue.setEType(EType.ENUM);
            }
            if (EType.STRING.toString().equals(typePayload.getType())) {
                finded = true;
                tableValue.setEType(EType.STRING);
            }
            if (EType.NUMBER.toString().equals(typePayload.getType())) {
                finded = true;
                tableValue.setEType(EType.NUMBER);
            }
            if (finded){
                int priority = 1;
                if (!customTable.getColumns().isEmpty()){
                    priority = customTable.getColumns().size()+1;
                }
                tableValue.setPriority(priority);
                Types newTableValue = tableColumnDAO.save(tableValue);
                Set<Types> tableValueSet = customTable.getColumns();
                tableValueSet.add(newTableValue);
                customTable.setColumns(tableValueSet);
                CustomTable resp = customTableDAO.save(customTable);
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
        CustomTablePayload customTablePayload = new CustomTablePayload();
        model.addAttribute("projectTablePayload", customTablePayload);
        return "constructor/createProjectTable";
    }

    @PostMapping("/createProjectTable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postCreate(@ModelAttribute("projectTablePayload") @Valid CustomTablePayload customTablePayload,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "constructor/createProjectTable";

        CustomTable customTable = customTableDAO.newProjectTable(customTablePayload);
        if (customTable ==null) {
            FieldError error = new FieldError("ProjectTablePayload","name","Имя не уникально");
            bindingResult.addError(error);
            return "constructor/createProjectTable";
        }
        return "redirect:/constructor/selectRoles/"+ customTable.getId();
    }

    @GetMapping("/selectRoles/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getSelectRoles(@PathVariable String id,Model model) {
        if (!customTableDAO.isPresent(id)){
            return "redirect:/constructor/index";
        }
        CustomTable customTable = customTableDAO.getCustomTableById(id);
        List<Role> roles = roleDAO.getRoles();
        model.addAttribute("roles",roles);
        model.addAttribute("projectTable", customTable);
        return "constructor/selectRoles";
    }


    @PostMapping("/selectRoles/{id}/addrole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postSelectRoles(@PathVariable String id,@RequestParam Long roleId,Model model) {
        Role role = roleDAO.getRoleById(roleId);
        CustomTable customTable = customTableDAO.getCustomTableById(id);
        if (customTable !=null){
            if (role!=null){
                customTable.getRoles().add(role);
                CustomTable resp = customTableDAO.save(customTable);
                model.addAttribute("projectTable",resp);
                model.addAttribute("roles",roleDAO.getRoles());
                return "redirect:/constructor/selectRoles/" + resp.getId();
            }
            return "redirect:/constructor/index";
        }
        return "redirect:/constructor/index";
    }

}

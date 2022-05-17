package com.baska.RSE.Controllers;

import com.baska.RSE.DAO.EnumValuesDAO;
import com.baska.RSE.DAO.CustomTableDAO;
import com.baska.RSE.DAO.RoleDAO;
import com.baska.RSE.DAO.TableColumnDAO;
import com.baska.RSE.Models.*;
import com.baska.RSE.Payload.Constructor.EnumPayload;
import com.baska.RSE.Payload.Constructor.TypePayload;
import com.baska.RSE.Payload.Constructor.CustomTablePayload;
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


    @PostMapping("/edit/{tableId}/editColumn/{columnId}/editEnum/{enumId}/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postSelectEnumAddColumn(@PathVariable("tableId") String tableId,
                                          @PathVariable("columnId") Long columnId,
                                          @PathVariable("enumId") Long enumId,
                                          @ModelAttribute("enumPayload") @Valid EnumPayload enumPayload,
                                          BindingResult bindingResult,
                                          Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types column = tableColumnDAO.getById(columnId);
        if (column == null) {
            return "redirect:/constructor/index";
        }
        EnumValues enumValues = enumValuesDAO.getById(enumId);
        if (enumValues == null) {
            return "redirect:/constructor/index";
        }
        if (bindingResult.hasErrors()) {
            List<EnumValues> enumValuesList = enumValuesDAO.getAll();
            EnumPayload enumPayload2 = new EnumPayload();
            model.addAttribute("enumValues", enumValues);
            model.addAttribute("enumPayload", enumPayload2);
            model.addAttribute("enumValuesList", enumValuesList);
            model.addAttribute("table", customTable);
            model.addAttribute("column", column);
            return "constructor/editEnumColumn";
        }
        List<String> stringList = enumValues.getEnumTypes();
        stringList.add(enumPayload.getName());
        enumValues.setEnumTypes(stringList);
        enumValuesDAO.save(enumValues);
        return "redirect:/constructor/edit/" + tableId + "/editColumn/" + columnId + "/editEnum/" + enumValues.getId();
    }

    @GetMapping("/edit/{tableId}/editColumn/{columnId}/editEnum/{enumId}/delete/{value}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getSelectEnumDeleteColumn(@PathVariable("tableId") String tableId,
                                            @PathVariable("columnId") Long columnId,
                                            @PathVariable("enumId") Long enumId,
                                            @PathVariable("value") String value) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types column = tableColumnDAO.getById(columnId);
        if (column == null) {
            return "redirect:/constructor/index";
        }
        EnumValues enumValues = enumValuesDAO.getById(enumId);
        if (enumValues == null) {
            return "redirect:/constructor/index";
        }
        List<String> values = enumValues.getEnumTypes();
        values.remove(value);
        enumValues.setEnumTypes(values);
        enumValuesDAO.save(enumValues);
        return "redirect:/constructor/edit/" + tableId + "/editColumn/" + columnId + "/editEnum/" + enumValues.getId();
    }


    @GetMapping("/edit/{tableId}/editColumn/{columnId}/editEnum/{enumId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getSelectEnumColumn(@PathVariable("tableId") String tableId,
                                      @PathVariable("columnId") Long columnId,
                                      @PathVariable("enumId") Long enumId,
                                      Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types column = tableColumnDAO.getById(columnId);
        if (column == null) {
            return "redirect:/constructor/index";
        }
        EnumValues enumValues = enumValuesDAO.getById(enumId);
        if (enumValues == null) {
            return "redirect:/constructor/index";
        }

        List<EnumValues> enumValuesList = enumValuesDAO.getAll();
        EnumPayload enumPayload = new EnumPayload();
        model.addAttribute("enumValues", enumValues);
        model.addAttribute("enumPayload", enumPayload);
        model.addAttribute("enumValuesList", enumValuesList);
        model.addAttribute("table", customTable);
        model.addAttribute("column", column);
        return "constructor/editEnumColumn";
    }

    @PostMapping("/edit/{tableId}/editColumn/{columnId}/editEnum")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postSelectEnumColumn(@PathVariable("tableId") String tableId,
                                       @PathVariable("columnId") Long columnId,
                                       @RequestParam("enumId") Long enumId) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types column = tableColumnDAO.getById(columnId);
        if (column == null) {
            return "redirect:/constructor/index";
        }
        EnumValues enumValues = enumValuesDAO.getById(enumId);
        if (enumValues == null) {
            return "redirect:/constructor/index";
        }
        column.setEnumValue(enumValues);
        tableColumnDAO.save(column);
        return "redirect:/constructor/edit/" + tableId + "/editColumn/" + columnId + "/editEnum/" + enumValues.getId();
    }


    @PostMapping("/edit/{tableId}/editColumn/{columnId}/selectEnum/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postSelectEnumAddColumn(@PathVariable("tableId") String tableId,
                                          @PathVariable("columnId") Long columnId,
                                          @ModelAttribute("enumPayload") @Valid EnumPayload enumPayload,
                                          BindingResult bindingResult,
                                          Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types column = tableColumnDAO.getById(columnId);
        if (column == null) {
            return "redirect:/constructor/index";
        }
        if (bindingResult.hasErrors()) {
            List<EnumValues> enumValuesList = enumValuesDAO.getAll();
            EnumPayload enumPayload2 = new EnumPayload();
            model.addAttribute("enumPayload", enumPayload2);
            model.addAttribute("enumValuesList", enumValuesList);
            model.addAttribute("table", customTable);
            model.addAttribute("column", column);
            return "constructor/selectEnum";
        }
        EnumValues enumValue = new EnumValues();
        enumValue.setName(enumPayload.getName());
        enumValue = enumValuesDAO.save(enumValue);
        column.setEnumValue(enumValue);
        tableColumnDAO.save(column);
        return "redirect:/constructor/edit/" + tableId + "/editColumn/" + columnId + "/editEnum/" + enumValue.getId();
    }


    @GetMapping("/edit/{tableId}/editColumn/{columnId}/selectEnum")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getSelectEnumColumn(@PathVariable("tableId") String tableId,
                                      @PathVariable("columnId") Long columnId,
                                      Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types column = tableColumnDAO.getById(columnId);
        if (column == null) {
            return "redirect:/constructor/index";
        }
        EnumValues enumValue = column.getEnumValue();
        List<EnumValues> enumValuesList = enumValuesDAO.getAll();
        EnumPayload enumPayload = new EnumPayload();
        model.addAttribute("enumPayload", enumPayload);
        model.addAttribute("enumValuesList", enumValuesList);
        model.addAttribute("table", customTable);
        model.addAttribute("column", column);
        return "constructor/selectEnumColumn";
    }
    /////////////////////////////////////////////


    @PostMapping("/edit/{tableId}/editProps/{propId}/editEnum/{enumId}/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postSelectEnumAdd(@PathVariable("tableId") String tableId,
                                    @PathVariable("propId") Long propId,
                                    @PathVariable("enumId") Long enumId,
                                    @ModelAttribute("enumPayload") @Valid EnumPayload enumPayload,
                                    BindingResult bindingResult,
                                    Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types prop = tableColumnDAO.getById(propId);
        if (prop == null) {
            return "redirect:/constructor/index";
        }
        EnumValues enumValues = enumValuesDAO.getById(enumId);
        if (enumValues == null) {
            return "redirect:/constructor/index";
        }
        if (bindingResult.hasErrors()) {
            List<EnumValues> enumValuesList = enumValuesDAO.getAll();
            EnumPayload enumPayload2 = new EnumPayload();
            model.addAttribute("enumValues", enumValues);
            model.addAttribute("enumPayload", enumPayload2);
            model.addAttribute("enumValuesList", enumValuesList);
            model.addAttribute("table", customTable);
            model.addAttribute("prop", prop);
            return "constructor/editEnum";
        }
        List<String> stringList = enumValues.getEnumTypes();
        stringList.add(enumPayload.getName());
        enumValues.setEnumTypes(stringList);
        enumValuesDAO.save(enumValues);
        return "redirect:/constructor/edit/" + tableId + "/editProps/" + propId + "/editEnum/" + enumValues.getId();
    }

    @GetMapping("/edit/{tableId}/editProps/{propId}/editEnum/{enumId}/delete/{value}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getSelectEnumDelete(@PathVariable("tableId") String tableId,
                                      @PathVariable("propId") Long propId,
                                      @PathVariable("enumId") Long enumId,
                                      @PathVariable("value") String value) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types prop = tableColumnDAO.getById(propId);
        if (prop == null) {
            return "redirect:/constructor/index";
        }
        EnumValues enumValues = enumValuesDAO.getById(enumId);
        if (enumValues == null) {
            return "redirect:/constructor/index";
        }
        List<String> values = enumValues.getEnumTypes();
        values.remove(value);
        enumValues.setEnumTypes(values);
        enumValuesDAO.save(enumValues);
        return "redirect:/constructor/edit/" + tableId + "/editProps/" + propId + "/editEnum/" + enumValues.getId();
    }


    @GetMapping("/edit/{tableId}/editProps/{propId}/editEnum/{enumId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getSelectEnum(@PathVariable("tableId") String tableId,
                                @PathVariable("propId") Long propId,
                                @PathVariable("enumId") Long enumId,
                                Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types prop = tableColumnDAO.getById(propId);
        if (prop == null) {
            return "redirect:/constructor/index";
        }
        EnumValues enumValues = enumValuesDAO.getById(enumId);
        if (enumValues == null) {
            return "redirect:/constructor/index";
        }

        List<EnumValues> enumValuesList = enumValuesDAO.getAll();
        EnumPayload enumPayload = new EnumPayload();
        model.addAttribute("enumValues", enumValues);
        model.addAttribute("enumPayload", enumPayload);
        model.addAttribute("enumValuesList", enumValuesList);
        model.addAttribute("table", customTable);
        model.addAttribute("prop", prop);
        return "constructor/editEnum";
    }

    @PostMapping("/edit/{tableId}/editProps/{propId}/editEnum")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postSelectEnum(@PathVariable("tableId") String tableId,
                                 @PathVariable("propId") Long propId,
                                 @RequestParam("enumId") Long enumId) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types prop = tableColumnDAO.getById(propId);
        if (prop == null) {
            return "redirect:/constructor/index";
        }
        EnumValues enumValues = enumValuesDAO.getById(enumId);
        if (enumValues == null) {
            return "redirect:/constructor/index";
        }
        prop.setEnumValue(enumValues);
        tableColumnDAO.save(prop);
        return "redirect:/constructor/edit/" + tableId + "/editProps/" + propId + "/editEnum/" + enumValues.getId();
    }


    @PostMapping("/edit/{tableId}/editProps/{propId}/selectEnum/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postSelectEnumAdd(@PathVariable("tableId") String tableId,
                                    @PathVariable("propId") Long propId,
                                    @ModelAttribute("enumPayload") @Valid EnumPayload enumPayload,
                                    BindingResult bindingResult,
                                    Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types prop = tableColumnDAO.getById(propId);
        if (prop == null) {
            return "redirect:/constructor/index";
        }
        if (bindingResult.hasErrors()) {
            List<EnumValues> enumValuesList = enumValuesDAO.getAll();
            EnumPayload enumPayload2 = new EnumPayload();
            model.addAttribute("enumPayload", enumPayload2);
            model.addAttribute("enumValuesList", enumValuesList);
            model.addAttribute("table", customTable);
            model.addAttribute("prop", prop);
            return "constructor/selectEnum";
        }
        EnumValues enumValue = new EnumValues();
        enumValue.setName(enumPayload.getName());
        enumValue = enumValuesDAO.save(enumValue);
        prop.setEnumValue(enumValue);
        tableColumnDAO.save(prop);
        return "redirect:/edit/" + tableId + "/editProps/" + propId + "/selectEnum/" + enumValue.getId();
    }


    @GetMapping("/edit/{tableId}/editProps/{propId}/selectEnum")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getSelectEnum(@PathVariable("tableId") String tableId,
                                @PathVariable("propId") Long propId,
                                Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types prop = tableColumnDAO.getById(propId);
        if (prop == null) {
            return "redirect:/constructor/index";
        }
        EnumValues enumValue = prop.getEnumValue();
        List<EnumValues> enumValuesList = enumValuesDAO.getAll();
        EnumPayload enumPayload = new EnumPayload();
        model.addAttribute("enumPayload", enumPayload);
        model.addAttribute("enumValuesList", enumValuesList);
        model.addAttribute("table", customTable);
        model.addAttribute("prop", prop);
        return "constructor/selectEnum";
    }


    @GetMapping("/edit/{tableId}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getDelete(@PathVariable String tableId) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        customTableDAO.deleteCustomTable(customTable);
        return "redirect:/constructor/tableList";
    }

    @PostMapping("/edit/{tableId}/editColumn/addColumn")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postAddColumn(@PathVariable String tableId,
                                @ModelAttribute("typePayload") @Valid TypePayload typePayload,
                                BindingResult bindingResult,
                                Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
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
            return "constructor/editColumn";
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
            priority = customTable.getColumns().size() + 1;
        }
        types.setPriority(priority);
        Types newTableValue = tableColumnDAO.save(types);
        Set<Types> tableValueSet = customTable.getColumns();
        tableValueSet.add(newTableValue);
        customTable.setColumns(tableValueSet);
        CustomTable resp = customTableDAO.save(customTable);
        return "redirect:/constructor/edit/" + resp.getId() + "/editColumn";
    }


    @GetMapping("/edit/{tableId}/editColumn/{columnId}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditColumnDelete(@PathVariable("tableId") String tableId,
                                      @PathVariable("columnId") Long columnId) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types type = tableColumnDAO.getById(columnId);
        if (type == null) {
            return "redirect:/constructor/index";
        }
        int priority = type.getPriority();
        Set<Types> typesSet = customTable.getColumns();
        typesSet.remove(type);
        Set<Types> newTable = new HashSet<>();
        for (Types el : typesSet) {
            if (el.getPriority() > priority) {
                el.setPriority(el.getPriority() - 1);
            }
            newTable.add(tableColumnDAO.save(el));
        }
        customTable.setColumns(newTable);
        CustomTable resp = customTableDAO.save(customTable);
        tableColumnDAO.delete(type);
        return "redirect:/constructor/edit/" + resp.getId() + "/editColumn";
    }


    @GetMapping("/edit/{tableId}/editColumn/{columnId}/up")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditColumnUp(@PathVariable("tableId") String tableId,
                                  @PathVariable("columnId") Long columnId) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types type = tableColumnDAO.getById(columnId);
        if (type == null) {
            return "redirect:/constructor/index";
        }
        if (type.getPriority() <= 1) {
            return "redirect:/constructor/edit/" + customTable.getId() + "/editColumn/";
        }
        Set<Types> typeSet = customTable.getColumns();
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
        return "redirect:/constructor/edit/" + resp.getId() + "/editColumn/";
    }

    @GetMapping("/edit/{tableId}/editColumn/{columnId}/down")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditColumnDown(@PathVariable("tableId") String tableId,
                                    @PathVariable("columnId") Long columnId) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types type = tableColumnDAO.getById(columnId);
        if (type == null) {
            return "redirect:/constructor/index";
        }
        if (type.getPriority() >= customTable.getProps().size()) {
            return "redirect:/constructor/edit/" + customTable.getId() + "/editColumn/";
        }
        Set<Types> typeSet = customTable.getColumns();
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
        return "redirect:/constructor/edit/" + resp.getId() + "/editColumn/";
    }


    @GetMapping("/edit/{tableId}/editColumn")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditColumn(@PathVariable String tableId,
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
        return "constructor/editColumn";
    }


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
        return "redirect:/constructor/edit/" + resp.getId() + "/editProps";
    }

    @PostMapping("/edit/{tableId}/editProps/addProp")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postAddProp(@PathVariable String tableId,
                              @ModelAttribute("typePayload") @Valid TypePayload typePayload,
                              BindingResult bindingResult,
                              Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
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
        if (!customTable.getProps().isEmpty()) {
            priority = customTable.getProps().size() + 1;
        }
        types.setPriority(priority);
        Types newTableValue = tableColumnDAO.save(types);
        Set<Types> tableValueSet = customTable.getProps();
        tableValueSet.add(newTableValue);
        customTable.setProps(tableValueSet);
        CustomTable resp = customTableDAO.save(customTable);
        return "redirect:/constructor/edit/" + resp.getId() + "/editProps";
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
            return "redirect:/constructor/edit/" + customTable.getId() + "/editProps/";
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
        return "redirect:/constructor/edit/" + resp.getId() + "/editProps/";
    }

    @GetMapping("/edit/{tableId}/editProps/{propId}/down")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditPropsDown(@PathVariable("tableId") String tableId,
                                   @PathVariable("propId") Long propId) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Types type = tableColumnDAO.getById(propId);
        if (type == null) {
            return "redirect:/constructor/index";
        }
        if (type.getPriority() >= customTable.getProps().size()) {
            return "redirect:/constructor/edit/" + customTable.getId() + "/editProps/";
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
        return "redirect:/constructor/edit/" + resp.getId() + "/editProps/";
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
    public String getEditProps(@PathVariable String tableId,
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
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("table", customTable);
            return "constructor/editTable";
        }
        if (!customTable.getName().equals(customTablePayload.getName())) {
            if (customTableDAO.isPresentByName(customTablePayload.getName())) {
                FieldError error = new FieldError("CustomTablePayload", "name", "Имя не уникально");
                bindingResult.addError(error);
                model.addAttribute("table", customTable);
                return "constructor/editTable";
            }
        }
        String bool = customTablePayload.getBool();
        if (bool == null)
            customTable.setEnabled(false);
        else
            customTable.setEnabled(true);
        customTable.setName(customTablePayload.getName());
        customTableDAO.save(customTable);
        return "redirect:/constructor/edit/" + customTable.getId();
    }

    @PostMapping("/edit/{tableId}/addRole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postAddRole(@PathVariable String tableId,
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

    @GetMapping("/edit/{tableId}/deleteRole/{roleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postDeleteRole(@PathVariable String tableId,
                                 @PathVariable Long roleId) {
        CustomTable customTable = customTableDAO.getCustomTableById(tableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        Role role = roleDAO.getRoleById(roleId);
        if (role == null) {
            return "redirect:/constructor/index";
        }
        Set<Role> roles = customTable.getRoles();
        roles.remove(role);
        customTable.setRoles(roles);
        customTableDAO.save(customTable);
        return "redirect:/constructor/edit/" + customTable.getId() + "/editRole";
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

    @GetMapping("/edit/{customTableId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getEditTable(@PathVariable("customTableId") String customTableId,
                               Model model) {
        CustomTable customTable = customTableDAO.getCustomTableById(customTableId);
        if (customTable == null) {
            return "redirect:/constructor/index";
        }
        CustomTablePayload customTablePayload = new CustomTablePayload();
        customTablePayload.setName(customTable.getName());
        customTablePayload.setBool(customTable.getEnabled().toString());
        model.addAttribute("CustomTablePayload", customTablePayload);
        model.addAttribute("table", customTable);
        return "constructor/editTable";
    }


    @GetMapping("/tableList")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getTableList(Model model) {
        List<CustomTable> customTableList = customTableDAO.getCustomTableList();
        model.addAttribute("tableList", customTableList);
        return "constructor/customTableList";
    }


    @GetMapping("/index")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getConstructor(Model model) {
        return "constructor/index";
    }

    @GetMapping("/createCustomTable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getCreate(Model model) {
        CustomTablePayload customTablePayload = new CustomTablePayload();
        model.addAttribute("customTablePayload", customTablePayload);
        return "constructor/createCustomTable";
    }

    @PostMapping("/createCustomTable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postCreate(@ModelAttribute("customTablePayload") @Valid CustomTablePayload customTablePayload,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "constructor/createCustomTable";

        CustomTable customTable = customTableDAO.newProjectTable(customTablePayload);
        if (customTable == null) {
            FieldError error = new FieldError("ProjectTablePayload", "name", "Имя не уникально");
            bindingResult.addError(error);
            return "constructor/createCustomTable";
        }
        return "redirect:/constructor/edit/" + customTable.getId();
    }
}

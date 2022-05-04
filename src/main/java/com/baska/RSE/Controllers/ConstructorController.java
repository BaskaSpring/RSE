package com.baska.RSE.Controllers;

import com.baska.RSE.Payload.Constructor.ProjectTablePayload;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class ConstructorController {

    @GetMapping("/constructor")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getConstructor(Model model, Principal principal) {
        int l = 1;
        return "Constructor/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getCreate(Model model, Principal principal) {
        int l = 1;
        return "Constructor/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postCreate(Model model, Principal principal) {
        model.addAttribute(new ProjectTablePayload());
        return "Constructor/create";
    }
}

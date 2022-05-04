package com.baska.RSE.Controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class UTOController {

    @GetMapping("/uto")
    @PreAuthorize("hasAuthority('UTO')")
    public String uto(Model model, Principal principal) {
        int l = 1;
        return "uto/index";
    }

}

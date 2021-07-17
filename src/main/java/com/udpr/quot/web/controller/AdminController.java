package com.udpr.quot.web.controller;

import com.udpr.quot.service.admin.AdminService;
import com.udpr.quot.web.dto.admin.AdminDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/admin/join")
    public String joinForm(Model model){
        model.addAttribute("form",new AdminDto());
        return "account/join";
    }

    @PostMapping("/admin/join")
    public String join(@ModelAttribute AdminDto dto){
        adminService.save(dto);
        return "redirect:/login";
    }
}

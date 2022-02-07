package com.udpr.quot.web.controller.admin;

import com.udpr.quot.config.auth.LoginUser;
import com.udpr.quot.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/admin/setting-icons")
    public String getIconSettingPage(Model model, @LoginUser SessionUser user){

        if(user!=null && user.getRole().getKey().equals("ADMIN")){
            model.addAttribute("user", user);
        }

        return "admin/settingIcons";
    }
}

package com.udpr.quot.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String login(HttpServletRequest request){
        String referrer = request.getHeader("Referer");
        if(!referrer.contains("/login")){
            request.getSession().setAttribute("prevPage", referrer);
        }
        return "account/login";

    }
}

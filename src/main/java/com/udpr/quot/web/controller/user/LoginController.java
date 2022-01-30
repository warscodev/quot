package com.udpr.quot.web.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/oauth_login")
    public String getLoginPage(HttpServletRequest request) {

        String uri = request.getHeader("Referer");

        if (uri != null && !uri.contains("/oauth_login")
                && !uri.contains("/oauth2.0/authorize")
                && !uri.contains("/signin")
                && !uri.contains("/o/oauth2")) {
            request.getSession().setAttribute("prevPage", uri);
        }
        return "account/login";
    }

}

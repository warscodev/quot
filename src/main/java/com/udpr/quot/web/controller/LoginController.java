package com.udpr.quot.web.controller;

import com.udpr.quot.config.auth.handler.CustomLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

package com.udpr.quot.config.auth.handler;

import com.udpr.quot.config.auth.dto.SessionUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && !((OAuth2User)authentication.getPrincipal()).getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
                response.sendRedirect("/");
            } else {
                request.getRequestDispatcher("/oauth_login").forward(request, response);
            }

    }

}

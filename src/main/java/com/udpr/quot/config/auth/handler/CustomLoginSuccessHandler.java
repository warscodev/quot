package com.udpr.quot.config.auth.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public CustomLoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String prevPage = (String) request.getSession().getAttribute("prevPage");

        if (prevPage != null) {
            request.getSession().removeAttribute("prevPage");
        }

        if (savedRequest != null && !savedRequest.getRedirectUrl().contains("/api")) {
            String targetUrl = savedRequest.getRedirectUrl();
            requestCache.removeRequest(request, response);
            System.out.println("targetUrl : " + targetUrl);
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else if(prevPage != null && !prevPage.equals("")) {
            System.out.println("prevPage : " + prevPage);
            redirectStrategy.sendRedirect(request, response, prevPage);

        } else {
            System.out.println("else");
            super.onAuthenticationSuccess(request, response, authentication);
        }

    }

}


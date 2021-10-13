package com.udpr.quot.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoginController {

 /* private static String authorizationRequestBaseUri
            = "oauth2/authorization";
    Map<String, String> oauth2AuthenticationUrls
            = new HashMap<>();

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;  */

    @GetMapping("/oauth_login")
    public String getLoginPage(HttpServletRequest request) {
    /*  Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE &&
                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));

                        model.addAttribute("urls", oauth2AuthenticationUrls);  */

        String uri = request.getHeader("Referer");

        if (uri != null && !uri.contains("/oauth_login")
                && !uri.contains("/oauth2.0/authorize)")
                && !uri.contains("/signin")
                && !uri.contains("/o/oauth2")) {
            request.getSession().setAttribute("prevPage",
                    request.getHeader("Referer"));
        }
        return "account/login";
    }

    /*@GetMapping("/login")
    public String login(HttpServletRequest request){

        String uri = request.getHeader("Referer");

        if (uri != null && !uri.contains("/login")) {
            request.getSession().setAttribute("prevPage",
                    request.getHeader("Referer"));
        }

        return "account/login";

    }*/

}

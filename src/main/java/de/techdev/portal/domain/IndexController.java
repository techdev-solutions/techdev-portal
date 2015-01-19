package de.techdev.portal.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class IndexController {

    @Value("${techdev.portal.google.client-id}")
    private String googleClientId;

    @Value("${techdev.portal.google.redirect-uri}")
    private String redirectUri;

    @Value("${techdev.portal.google.token-url}")
    private String tokenUrl;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("googleClientId", googleClientId);
        model.addAttribute("redirectUri", redirectUri);
        model.addAttribute("tokenUrl", tokenUrl);
        return "index";
    }

    @RequestMapping(value = "/showUser")
    public String overview(Principal principal, Model model) {
        model.addAttribute("principal", principal);
        return "showUser";
    }
}



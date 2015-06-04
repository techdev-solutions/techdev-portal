package de.techdev.portal.domain;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Profile("!google-login")
@RequestMapping("/login")
public class FormAuthenticationIndexController {

    @RequestMapping(method = RequestMethod.GET)
    public String login() {
        return "form/login";
    }
}



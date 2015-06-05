package de.techdev.portal.domain;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Profile("google-login")
public class GoogleAuthenticationIndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "google/index";
    }

    @RequestMapping(value = "/loggedOut", method = RequestMethod.GET)
    public String loggedOut() {
        return "google/loggedOut";
    }
}
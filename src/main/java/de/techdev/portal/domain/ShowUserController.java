package de.techdev.portal.domain;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class ShowUserController {

    @RequestMapping(value = "/showUser", method = RequestMethod.GET)
    public String overview(Principal principal, Model model) {
        model.addAttribute("principal", principal);
        return "showUser";
    }

}

package de.techdev.portal.domain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class LandingPageController {

    @RequestMapping("/landing")
    public ModelAndView showLandingPage(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("landingPage");
        modelAndView.addObject("userName", principal.getName());
        return modelAndView;
    }

}

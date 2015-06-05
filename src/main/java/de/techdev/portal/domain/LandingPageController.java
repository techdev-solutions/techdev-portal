package de.techdev.portal.domain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LandingPageController {

    @RequestMapping("/landing")
    public ModelAndView showLandingPage() {
        return new ModelAndView("landingPage");
    }

}

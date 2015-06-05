package de.techdev.portal.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ListUserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/listUsers", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView listUsers() {
        ModelAndView modelAndView = new ModelAndView("domain/users/listUsers");
        modelAndView.addObject("users", userRepository.findAll());
        return modelAndView;
    }
}

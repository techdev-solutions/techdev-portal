package de.techdev.portal.domain.user.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/createUser")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CreateUserController {

    @Autowired
    private CreateUserService createUserService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showNewUserForm() {
        return new ModelAndView("domain/users/newUser", "createUserRequest", new CreateUserRequest());
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createNewUser(@Valid CreateUserRequest createUserRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "domain/users/newUser";
        }

        try {
            createUserService.createNewUser(createUserRequest);
        } catch (UserExistsException e) {
            bindingResult.rejectValue("email", "foo.bar.error.code", "User already exists.");
            return "domain/users/newUser";
        }

        return "redirect:/listUsers";
    }

}


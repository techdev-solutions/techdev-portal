package de.techdev.portal.domain.user.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/editUser")
@PreAuthorize("hasRole('ROLE_ADMIN')")
class UpdateUserController {

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Autowired
    private TokenDeleter tokenDeleter;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showEditForm(@RequestParam("username") String username) {
        ModelAndView modelAndView = new ModelAndView("domain/users/editUser");
        modelAndView.addObject("user", new UpdateUserModel(userDetailsManager.loadUserByUsername(username)));
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateUser(@RequestParam("username") String username, @Valid UpdateUserRequest updateUserRequest) {
        UserDetails user = new User(username, "", updateUserRequest.isEnabled(), true, true, true, updateUserRequest.getRoles());
        userDetailsManager.updateUser(user);

        // Actually this only needs to be done when the user is disabled or the roles change. But for now this is all that
        // can happen and we are a bit lazy, so delete all tokens every time.
        tokenDeleter.deleteTrackrAngularClientTokens(username);
        return "redirect:/listUsers";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleUserNotFoundException(UsernameNotFoundException e) {
        return new ModelAndView("domain/users/notFound", "message", e.getMessage());
    }
}


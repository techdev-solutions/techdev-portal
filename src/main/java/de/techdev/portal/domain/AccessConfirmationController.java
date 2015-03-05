package de.techdev.portal.domain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Displays the oauth confirmation page (do you want to allow client xy to access your data?)
 */
@Controller
// The session attributes contain the authorization request that Spring Security OAuth2 made and then forwarded to this controller.
@SessionAttributes("authorizationRequest")
public class AccessConfirmationController {

    @RequestMapping("/oauth/confirm_access")
    public ModelAndView confirmAccess(Map<String, Object> model) {
        return new ModelAndView("oauth/confirm_access", model);
    }

}

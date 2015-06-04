package de.techdev.portal.domain.user.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import static java.util.Collections.singletonList;

@Service
class CreateUserService {

    @Autowired
    private UserDetailsManager userDetailsManager;

    User createNewUser(CreateUserRequest request) throws UserExistsException {
        if (userDetailsManager.userExists(request.getEmail())) {
            throw new UserExistsException();
        }

        // todo get authorities from the request
        User user = new User(request.getEmail(), "", request.isEnabled(), true, true, true, singletonList(new SimpleGrantedAuthority("ROLE_EMPLOYEE")));
        userDetailsManager.createUser(user);

        if (request.isWithTrackr()) {

        }

        return user;
    }
}

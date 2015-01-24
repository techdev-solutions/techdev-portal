package de.techdev.portal.authentication.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import static java.util.Arrays.asList;

/**
 * Loads a Techdev user from the database.
 *
 * If the user does not exist and the email address ends with @techdev.de it is created in the database and needs to get unlocked.
 */
@Service
@Profile("google-login")
public class TechdevAuthenticationManager implements AuthenticationManager {

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String emailAddress = authentication.getPrincipal().toString(); // recommended by spring!
        if(!userDetailsManager.userExists(emailAddress)) {
            return handleNotExistentUser(emailAddress);
        } else {
            UserDetails userDetails = userDetailsManager.loadUserByUsername(emailAddress);
            if (!userDetails.isEnabled()) {
                throw new DisabledException("Account is disabled.");
            }
            return fromUserDetails(userDetails);
        }
    }

    private Authentication handleNotExistentUser(String emailAddress) {
        if (!emailAddress.endsWith("@techdev.de")) {
            throw new NotATechdevEmailException();
        }
        User user = new User(emailAddress, "", false, true, true, true, asList(new SimpleGrantedAuthority("ROLE_EMPLOYEE")));
        userDetailsManager.createUser(user);
        throw new DisabledException("Account is created and needs to be activated.");
    }

    private UsernamePasswordAuthenticationToken fromUserDetails(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }

}

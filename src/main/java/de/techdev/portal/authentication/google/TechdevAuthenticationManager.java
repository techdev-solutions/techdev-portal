package de.techdev.portal.authentication.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

/**
 * Loads a Techdev user from the database.
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
            throw new UsernameNotFoundException("Account does not exist.");
        } else {
            UserDetails userDetails = userDetailsManager.loadUserByUsername(emailAddress);
            if (!userDetails.isEnabled()) {
                throw new DisabledException("Account is disabled.");
            }
            return fromUserDetails(userDetails);
        }
    }

    private UsernamePasswordAuthenticationToken fromUserDetails(UserDetails userDetails) {
        // We need to use the whole user details as the principal because the resource servers expect it there (and not only a string)!
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

}
